package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

/*
 * A rudimentary "rough" material.
 * Not very sophisticated, pretty unrigorous mathematically/physically
 * NEVER use (after I implement proper microfacets...)
 */
public class RoughMaterial extends MirrorMaterial {

	private SampleableScalar roughness;

	public RoughMaterial(Sampleable color, SampleableScalar roughness) {
		super(color);
		this.roughness = roughness;
	}
	
	@Override
	public double BRDF(Vector incident, Vector outgoing, Vector normal) {
		return 0; // this is totally bogus, but:
		          // it doesn't matter since no light sampling = no BRDF evaluation
	}
	
	@Override
	public Vector sampleBRDF(Vector incident, Hit hit) {
		Vector rand = Vector.uniformInHemisphere().times(Math.random() * roughness.sampleScalar(hit.textureCoordinates.x, hit.textureCoordinates.y));
		return super.sampleBRDF(incident, hit).iadd(rand).normalize();
	}

}
