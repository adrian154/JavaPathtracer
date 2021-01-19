package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public abstract class BaseMaterial implements Material {

	protected Sampleable color;
	
	public BaseMaterial(Sampleable color) {
		this.color = color;
	}
	
	public Vector getColor(double u, double v) {
		return color.sample(u, v);
	}
	
}
