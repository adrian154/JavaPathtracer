package com.JavaPathtracer.material;

import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;
import com.JavaPathtracer.pattern.SampleableDouble;

/*
 * A rudimentary "rough" material.
 * Not very sophisticated, pretty unrigorous mathematically/physically
 * NEVER use (after I implement proper microfacets...)
 */
public class GlossyMaterial extends MirrorMaterial {

	private SampleableDouble roughness;

	public GlossyMaterial(Sampleable color, SampleableDouble roughness) {
		super(color);
		this.roughness = roughness;
	}
	
	@Override
	public double BRDF(Vector incident, Vector outgoing, Vector normal, Vector textureCoordinates) {
		return 1 / outgoing.dot(normal);
	}
	
	@Override
	public Vector sample(Vector incident, Hit hit) {
		Vector rand = Vector.uniformInHemisphere().times(ThreadLocalRandom.current().nextDouble() * roughness.sampleScalar(hit.textureCoordinates.x, hit.textureCoordinates.y));
		return super.sample(incident, hit).iadd(rand).normalize();
	}

	@Override
	public String toString() {
		return String.format("Rough (%s)", color.toString());
	}
	
}
