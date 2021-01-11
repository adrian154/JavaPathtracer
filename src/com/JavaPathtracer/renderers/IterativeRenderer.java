package com.JavaPathtracer.renderers;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import com.JavaPathtracer.IterativePathtracer;
import com.JavaPathtracer.material.Texture;

public class IterativeRenderer extends ParallelRenderer {

	private IterativePathtracer pathtracer;

	public IterativeRenderer(IterativePathtracer pathtracer, int threads, int tiles) {
		super(pathtracer, threads, tiles);
		this.pathtracer = pathtracer;
	}

	@Override
	public void render(Texture output) {

		for (int iter = 1; iter < 720; iter++) {

			CountDownLatch latch = new CountDownLatch(this.tiles * this.tiles);

			int tileWidth = output.getWidth() / this.tiles;
			int tileHeight = output.getHeight() / this.tiles;
			for (int x = 0; x < this.tiles; x++) {
				for (int y = 0; y < this.tiles; y++) {
					this.executorService.submit(new IterativeRenderTask(this.pathtracer, x * tileWidth, y * tileHeight,
							(x + 1) * tileWidth, (y + 1) * tileHeight, output, latch, iter));
				}
			}

			try {
				latch.await();
				output.saveToFile(new File("timelapse/iteration_" + iter + ".png"));
			} catch (InterruptedException excep) {
				executorService.shutdownNow();
				break;
			}

		}

	}

}
