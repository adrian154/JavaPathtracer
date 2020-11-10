package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class RoughMaterial extends Material {

	private SampleableScalar roughness;
	
	public RoughMaterial(ISampleable color, ISampleable emission, SampleableScalar roughness) {
		super(color, emission);
		this.roughness = roughness;
	}
	
	public Vector scatter(double u, double v, Vector incident, Vector normal) {
		return incident.minus(normal.times(2 * normal.dot(incident)));
	}
	
	public boolean doDotProduct(double u, double v) {
		return false;
	}
	
}
