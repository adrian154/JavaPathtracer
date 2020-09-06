package com.JavaPathtracer.renderers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.material.Texture;

public class ParallelRenderer extends Renderer {

	private Raytracer raytracer;
	private ExecutorService executorService;
	private final int threads;
	private final int tiles;

	public ParallelRenderer(Raytracer raytracer, int tiles) {
		this.raytracer = raytracer;
		this.tiles = tiles;
		this.threads = tiles * tiles;
		this.executorService = Executors.newFixedThreadPool(this.threads);
	}
	
	public void render(Texture output) {
		
		CountDownLatch latch = new CountDownLatch(this.threads);
		
		int tileWidth = output.getWidth() / this.tiles;
		int tileHeight = output.getHeight() / this.tiles;
		for(int x = 0; x < this.tiles; x++) {
			for(int y = 0; y < this.tiles; y++) {
				this.executorService.execute(new RenderTask(
					this.raytracer,
					x * tileWidth,
					y * tileHeight,
					(x + 1) * tileWidth,
					(y + 1) * tileHeight,
					output,
					latch
				));
			}
		}
		
		try {
			latch.await();
		} catch(InterruptedException excep) {
			throw new RuntimeException(excep);
		}
		
		executorService.shutdownNow();
		
	}
	
	public void setraytracer(Raytracer raytracer) {
		this.raytracer = raytracer;
	}
	
}