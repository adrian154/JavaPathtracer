package com.JavaPathtracer.renderer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.tonemapping.IToneMapper;
import com.JavaPathtracer.tonemapping.LinearTonemapper;

public class Renderer {

	protected Scene scene;
	protected Camera camera;
	protected Raytracer raytracer;
	protected IToneMapper toneMapper;
	protected int tiles, threads, samples;
	
	public Renderer(Scene scene, Raytracer raytracer, IToneMapper mapper, int tiles, int threads, int samples) {
		this.scene = scene;
		this.raytracer = raytracer;
		this.toneMapper = mapper;
		this.tiles = tiles;
		this.threads = threads;
		this.samples = samples;
	}
	
	public Renderer(Scene scene,  Raytracer raytracer, int tiles, int samples, IToneMapper tonemapper) {
		this(scene, raytracer, tonemapper, tiles, Runtime.getRuntime().availableProcessors(), samples);
	}
	
	public Renderer(Scene scene, Raytracer raytracer, int tiles, int samples) {
		this(scene, raytracer, new LinearTonemapper(), tiles, Runtime.getRuntime().availableProcessors(), samples);
	}
	
	public void render(Texture output) throws InterruptedException {
		
		ExecutorService executor = Executors.newFixedThreadPool(this.threads);
		CountDownLatch latch = new CountDownLatch(this.tiles * this.tiles);

		// The scene is split into tiles for rendering.
		// FIXME: if the output size doesn't divide by the number of tiles part of the image isn't rendered.
		int tileWidth = output.getWidth() / tiles;
		int tileHeight = output.getHeight() / tiles;
		for(int x = 0; x < tiles; x++) {
			for(int y = 0; y < tiles; y++) {
				executor.execute(new RenderTask(
						x * tileWidth,
						y * tileHeight,
						x * tileWidth + tileWidth,
						y * tileHeight + tileHeight,
						output,
						latch
				));
			}
		}
		
		executor.shutdown();
		latch.await();
		
	}
	
	protected class RenderTask implements Runnable {

		protected int startX, endX;
		protected int startY, endY;
		protected Texture output;
		protected CountDownLatch latch;
		
		public RenderTask(int startX, int startY, int endX, int endY, Texture output, CountDownLatch latch) {
			this.startX = startX; this.startY = startY;
			this.endX = endX; this.endY = endY;
			this.output = output;
			this.latch = latch;
		}
		
		@Override
		public void run() {
			
			// calculate how much each pixel should be jittered
			int maxDim = Math.min(output.getWidth(), output.getHeight());
			double pixelWidth = 2.0 / output.getWidth();
			double pixelHeight = 2.0 / output.getHeight();
		
			Camera camera = scene.getCamera();
			
			for(int x = startX; x < endX; x++) {
				for(int y = startY; y < endY; y++) {
	
					// set the pixel green first, to indicate it's being rendered
					output.set(x, output.getHeight() - y - 1, Vector.G);
					
					// sample
					Vector result = new Vector();
					for(int i = 0; i < samples; i++) {
						Ray ray = camera.getCameraRay(x, y, maxDim, pixelWidth, pixelHeight);
						result = result.plus(raytracer.traceRay(scene, ray));
					}
					
					// set the pixel to the pathtraced value
					output.set(x, output.getHeight() - y - 1, toneMapper.map(result.divBy(samples)));
					
				}
			}

			latch.countDown();
			
		}
		
	}
	
}