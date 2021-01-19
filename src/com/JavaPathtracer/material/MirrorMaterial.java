package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public class MirrorMaterial extends BRDFMaterial {

	public MirrorMaterial(Sampleable color) {
		super(color);
	}

	public double BRDF(Vector incident, Vector outgoing, Vector normal) {
		return outgoing.dot(normal);
	}
	
	public Vector sampleBRDF(Vector incident, Hit hit) {
		return incident.minus(hit.normal.times(2 * hit.normal.dot(incident)));
	}
	
	public boolean sampleLights() {
		return false;
	}
	
}
