package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class MirrorMaterial extends Material {

	public MirrorMaterial(Sampleable color, Sampleable emission) {
		super(color, emission);
	}
	
	public Vector scatter(double u, double v, Vector incident, Vector normal) {
		return incident.minus(normal.times(2 * normal.dot(incident)));
	}
	
	public boolean doDotProduct(double u, double v) {
		return false;
	}
	
}
