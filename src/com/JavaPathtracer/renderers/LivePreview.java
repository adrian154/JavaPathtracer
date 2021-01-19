package com.JavaPathtracer.renderers;

import com.JavaPathtracer.material.Texture;

public class LivePreview {

	private int scale;
	private Texture output;
	private Renderer renderer;
	private LivePreviewFrame frame;

	public LivePreview(Texture output, Renderer renderer, int scale) {
		this.output = output;
		this.renderer = renderer;
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

	public Renderer getRenderer() {
		return this.renderer;
	}
	
}