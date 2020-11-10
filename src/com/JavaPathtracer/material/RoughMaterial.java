package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class RoughMaterial extends Material {

	private SampleableScalar roughness;
	
	public RoughMaterial(ISampleable color, ISampleable emission, SampleableScalar roughness) {
		super(color, emission);
		this.roughness = roughness;
	}
	
	public Vector scatter(double u, double v, Vector incident, Vector normal) {
		Vector reflect = incident.minus(normal.times(2 * normal.dot(incident)));
		Vector rough = Vector.uniformInHemisphere().times(roughness.sampleScalar(u, v));
		return (reflect.plus(rough)).normalize();
	}
	
	public boolean doDotProduct(double u, double v) {
		return false;
	}
	
}
