package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public abstract class Material implements IMaterial {

	private ISampleable color;
	private ISampleable emission;
	
	public Material(ISampleable color, ISampleable emission) {
		this.color = color;
		this.emission = emission;
	}
	
	public Vector getColor(double u, double v) {
		return color.sample(u, v);
	}
	
	public Vector getEmission(double u, double v) {
		return emission.sample(u, v);
	}
	
}
