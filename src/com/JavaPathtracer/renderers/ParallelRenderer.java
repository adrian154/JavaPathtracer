package com.JavaPathtracer.renderers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.material.Texture;

public class ParallelRenderer extends Renderer {

	protected Raytracer raytracer;
	protected ExecutorService executorService;
	protected final int threads;
	protected final int tiles;
	
	public ParallelRenderer(Raytracer raytracer, int tiles) {
		this(raytracer, Runtime.getRuntime().availableProcessors(), tiles);
	}

	public ParallelRenderer(Raytracer raytracer, int threads, int tiles) {
		this.raytracer = raytracer;
		this.tiles = tiles;
		this.threads = threads;
		this.executorService = Executors.newFixedThreadPool(this.threads);
	}

	@Override
	public void render(Texture output) {

		CountDownLatch latch = new CountDownLatch(this.tiles * this.tiles);

		int tileWidth = output.getWidth() / this.tiles;
		int tileHeight = output.getHeight() / this.tiles;
		for (int x = 0; x < this.tiles; x++) {
			for (int y = 0; y < this.tiles; y++) {
				this.executorService.submit(new RenderTask(this.raytracer, x * tileWidth, y * tileHeight,
						(x + 1) * tileWidth, (y + 1) * tileHeight, output, latch));
			}
		}

		try {
			latch.await();
		} catch (InterruptedException excep) {
			throw new RuntimeException(excep);
		}

		executorService.shutdownNow();

	}

	public void setraytracer(Raytracer raytracer) {
		this.raytracer = raytracer;
	}

}