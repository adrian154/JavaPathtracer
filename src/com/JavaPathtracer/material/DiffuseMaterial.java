package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public class DiffuseMaterial extends BRDFMaterial {

	public DiffuseMaterial(Sampleable color) {
		super(color);
	}
	
	@Override
	public Vector sampleBRDF(Vector incident, Hit hit) {
		
		Vector random = Vector.uniformInHemisphere();
		Vector bvx = hit.normal.getOrthagonal();
		Vector bvy = hit.normal;
		Vector bvz = bvy.cross(bvx);

		return Vector.localToWorldCoords(random, bvx, bvy, bvz);
	
	}
	
	@Override
	public double BRDF(Vector incident, Vector outgoing, Vector normal) {
		return 1;
	}
	
	@Override
	public boolean sampleLights() {
		return true;
	}

}
