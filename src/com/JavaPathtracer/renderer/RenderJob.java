package com.JavaPathtracer.renderer;

import java.util.concurrent.CountDownLatch;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.material.Texture;

// Used to get information about a render operation
public class RenderJob {
	
	protected long initJobs;
	protected CountDownLatch latch;
	protected Renderer renderer;
	protected Texture output;
	
	public RenderJob(Renderer renderer, CountDownLatch latch, Texture output) {
		this.renderer = renderer;
		this.latch = latch;
		this.output = output;
		this.initJobs = latch.getCount();
	}
	
	public Raytracer getRaytracer() {
		return renderer.getRaytracer();
	}
	
	public int getSamples() {
		return renderer.getSamples();
	}
	
	public int getRays() {
		return renderer.getRaytracer().getRays();
	}
	
	public int getThreads() {
		return renderer.getThreads();
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public long getRemainingTiles() {
		return latch.getCount();
	}
	
	public long getCompletedTiles() {
		return initJobs - latch.getCount();
	}
	
	public long getInitTiles() {
		return initJobs;
	}
	
	public Texture getOutput() {
		return output;
	}
	
	public void await() throws InterruptedException {
		latch.await();
	}
	
}
