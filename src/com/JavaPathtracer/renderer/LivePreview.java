package com.JavaPathtracer.renderer;

import com.JavaPathtracer.material.Texture;

public class LivePreview {

	private int scale;
	private Texture output;
	private RenderJob job;
	private LivePreviewFrame frame;

	public LivePreview(RenderJob renderJob, int scale) {
		this.output = renderJob.getOutput();
		this.job = renderJob;
		this.scale = scale;
	}

	public void start() {
		this.frame = new LivePreviewFrame(this, scale);
	}

	public LivePreviewFrame getPreviewFrame() {
		return this.frame;
	}
	
	public Texture getImage() {
		return this.output;
	}

	public RenderJob getRenderJob() {
		return this.job;
	}
	
}