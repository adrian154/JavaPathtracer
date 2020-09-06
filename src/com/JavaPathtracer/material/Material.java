package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class Material {

	private Sampleable color;
	private Sampleable emission;
	
	public Material(Sampleable color, Sampleable emission) {
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
