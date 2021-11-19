package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;

public abstract class BaseMaterial implements Material {

	private Sampleable color;
	
	public BaseMaterial(Sampleable color) {
		this.color = color;
	}
	
	public Vector getColor(double u, double v) {
		return color.sample(u, v);
	}
	
}
