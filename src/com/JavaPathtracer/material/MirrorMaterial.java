package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;

public class MirrorMaterial extends BRDFMaterial {

	public MirrorMaterial(Sampleable color) {
		super(color);
	}
	
	public MirrorMaterial() {
		this(new Vector(1.0, 1.0, 1.0));
	}

	// since the brdf is always analytically sampled..
	// we can use a bogus BRDF even though the real BRDF is a delta func
	public double BRDF(Vector incident, Vector outgoing, Vector normal, Vector textureCoordinates) {
		return 1 / outgoing.dot(normal);
	}
	
	public static Vector reflect(Vector normal, Vector incident) {
		return incident.minus(normal.times(2 * normal.dot(incident)));
	}
	
	public Vector sample(Vector incident, Hit hit) {
		return MirrorMaterial.reflect(hit.normal, incident);
	}
	
	public boolean sampleLights() {
		return false;
	}
	
	@Override
	public String toString() {
		return String.format("Mirror %s", color.toString());
	}
	
}
