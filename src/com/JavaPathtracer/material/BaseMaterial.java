package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;

public abstract class BaseMaterial implements Material {

	protected Sampleable color;
	
	public BaseMaterial(Sampleable color) {
		this.color = color;
	}
	
	public Vector getColor(Vector textureCoords) {
		return color.sample(textureCoords);
	}
	
	@Override
	public Vector getDebugColor(Vector textureCoords) {
		return this.getColor(textureCoords);
	}
	
}
