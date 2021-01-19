package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public class MirrorMaterial extends BRDFMaterial {

	public MirrorMaterial(Sampleable color) {
		super(color);
	}
	
	public MirrorMaterial() {
		this(new Vector(1.0, 1.0, 1.0));
	}

	// since the brdf is always analytically sampled..
	// we can use a bogus BRDF even though the real BRDF is a delta func
	public double BRDF(Vector incident, Vector outgoing, Vector normal) {
		return 1 / outgoing.dot(normal);
	}
	
	public Vector sampleBRDF(Vector incident, Hit hit) {
		return incident.minus(hit.normal.times(2 * hit.normal.dot(incident)));
	}
	
	public boolean sampleLights() {
		return false;
	}
	
}
