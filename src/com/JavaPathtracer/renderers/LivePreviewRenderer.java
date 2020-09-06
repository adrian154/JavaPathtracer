package com.JavaPathtracer.renderers;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.material.Texture;

public class LivePreviewRenderer extends ParallelRenderer {

	private int scale;
	
	public LivePreviewRenderer(Raytracer pathtracer, int tiles, int scale) {
		super(pathtracer, tiles);
		this.scale = scale;
	}
	
	@Override
	public void render(Texture output) {
		LivePreviewFrame frame = new LivePreviewFrame(output, scale);
		super.render(output);
	}
	
}