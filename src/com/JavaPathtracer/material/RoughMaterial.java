package com.JavaPathtracer.material;

import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

/*
 * A rudimentary "rough" material.
 * Not very sophisticated, pretty unrigorous mathematically/physically
 * NEVER use (after I implement proper microfacets...)
 */
public class RoughMaterial extends MirrorMaterial {

	private SampleableDouble roughness;

	public RoughMaterial(Sampleable color, SampleableDouble roughness) {
		super(color);
		this.roughness = roughness;
	}
	
	@Override
	public double BRDF(Vector incident, Vector outgoing, Vector normal) {
		return 1 / outgoing.dot(normal);
	}
	
	@Override
	public Vector sampleBRDF(Vector incident, Hit hit) {
		Vector rand = Vector.uniformInHemisphere().times(ThreadLocalRandom.current().nextDouble() * roughness.sampleScalar(hit.textureCoordinates.x, hit.textureCoordinates.y));
		return super.sampleBRDF(incident, hit).iadd(rand).normalize();
	}

}
