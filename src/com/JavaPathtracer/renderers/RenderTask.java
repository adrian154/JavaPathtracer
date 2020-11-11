package com.JavaPathtracer.renderers;

import java.util.concurrent.CountDownLatch;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.material.Texture;

public class RenderTask implements Runnable {

	protected Raytracer raytracer;
	protected int startX;
	protected int startY;
	protected int endX;
	protected int endY;
	protected Texture output;
	protected CountDownLatch latch;
	
	public RenderTask(Raytracer raytracer, int startX, int startY, int endX, int endY, Texture output, CountDownLatch latch) {
		this.raytracer = raytracer;
		this.startX = startX;
		this.endX = endX;
		this.startY = startY;
		this.endY = endY;
		this.output = output;
		this.latch = latch;
	}
	
	public void run() {
		System.out.printf("Starting: (%d,%d), (%d,%d)\n", startX, startY, endX, endY);
		this.raytracer.pathtraceTile(this.output, startX, startY, endX, endY);
		System.out.printf("Done! (%d remaining) (%d,%d), (%d,%d)\n", latch.getCount() - 1, startX, startY, endX, endY);
		latch.countDown();
	}
	
}
