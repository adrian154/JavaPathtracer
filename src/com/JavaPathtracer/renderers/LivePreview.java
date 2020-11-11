package com.JavaPathtracer.renderers;

import com.JavaPathtracer.material.Texture;

public class LivePreview {

	private int scale;
	private Texture output;
	private LivePreviewFrame frame;
	
	public LivePreview(Texture output, int scale) {
		this.output = output;
		this.scale = scale;
	}

	public void start() {
		this.frame = new LivePreviewFrame(output, scale);
	}

	public LivePreviewFrame getPreviewFrame() {
		return this.frame;
	}
	
}