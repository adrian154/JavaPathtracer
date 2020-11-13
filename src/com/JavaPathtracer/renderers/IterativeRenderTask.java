package com.JavaPathtracer.renderers;

import java.util.concurrent.CountDownLatch;

import com.JavaPathtracer.IterativePathtracer;
import com.JavaPathtracer.material.Texture;

public class IterativeRenderTask extends RenderTask {

	private int iteration;
	private IterativePathtracer iterTracer;

	public IterativeRenderTask(IterativePathtracer pathtracer, int startX, int startY, int endX, int endY, Texture output, CountDownLatch latch, int iteration) {
		super(pathtracer, startX, startY, endX, endY, output, latch);
		this.iteration = iteration;
		this.iterTracer = pathtracer;
	}
	
	public void run() {
		iterTracer.pathtraceTile(output, startX, startY, endX, endY, this.iteration);
		latch.countDown();
	}
	
}
