package com.JavaPathtracer.renderer;

import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.tonemapping.IToneMapper;
import com.JavaPathtracer.tonemapping.LinearTonemapper;

public class Renderer {

	public final Scene scene;
	public final Raytracer raytracer;
	public final IToneMapper toneMapper;
	public final int tiles, threads, samples;
	
	public Renderer(Scene scene, Raytracer raytracer, int tiles, int threads, int samples, IToneMapper mapper) {
		this.scene = scene;
		this.raytracer = raytracer;
		this.toneMapper = mapper;
		this.tiles = tiles;
		this.threads = threads;
		this.samples = samples;
	}
	
	public Renderer(Scene scene,  Raytracer raytracer, int tiles, int samples, IToneMapper tonemapper) {
		this(scene, raytracer, tiles, Runtime.getRuntime().availableProcessors(), samples, tonemapper);
	}
	
	public Renderer(Scene scene, Raytracer raytracer, int tiles, int samples) {
		this(scene, raytracer, tiles, Runtime.getRuntime().availableProcessors(), samples, new LinearTonemapper());
	}
	
	public RenderJob render(BufferedImage output) throws InterruptedException {
		
		ExecutorService executor = Executors.newFixedThreadPool(this.threads);
		CountDownLatch latch = new CountDownLatch(this.tiles * this.tiles);

		// The scene is split into tiles for rendering.
		// FIXME: if the output size doesn't divide by the number of tiles part of the image isn't rendered.
		int tileWidth = output.getWidth() / tiles;
		int tileHeight = output.getHeight() / tiles;
		for(int x = 0; x < tiles; x++) {
			for(int y = 0; y < tiles; y++) {
				executor.execute(new RenderTileTask(
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
		return new RenderJob(latch, output, this);
		
	}
	
	public static class RenderJob {
		
		private CountDownLatch latch;
		public final BufferedImage output;
		public final Renderer renderer;
		
		public RenderJob(CountDownLatch latch, BufferedImage output, Renderer renderer) {
			this.latch = latch;
			this.output = output;
			this.renderer = renderer;
		}
		
		public void await() throws InterruptedException {
			this.latch.await();
		}
		
	}
	
	private class RenderTileTask implements Runnable {

		protected int startX, endX;
		protected int startY, endY;
		protected BufferedImage output;
		protected CountDownLatch latch;
		
		public RenderTileTask(int startX, int startY, int endX, int endY, BufferedImage output, CountDownLatch latch) {
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
					output.setRGB(x, output.getHeight() - y - 1, 0x00ff00);
					
					// sample
					Vector result = new Vector();
					for(int i = 0; i < samples; i++) {
						Ray ray = camera.getCameraRay(x, y, maxDim, pixelWidth, pixelHeight);
						result = result.plus(raytracer.traceRay(scene, ray));
					}
					
					// set the pixel to the pathtraced value
					output.setRGB(x, output.getHeight() - y - 1, toneMapper.map(result.divBy(samples)).toRGB());
					
				}
			}

			latch.countDown();
			
		}
		
	}
	
}