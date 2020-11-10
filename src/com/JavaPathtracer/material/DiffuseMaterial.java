package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class DiffuseMaterial extends Material {
	
	public DiffuseMaterial(Sampleable color, Sampleable material) {
		super(color, material);
	}
	
	public Vector scatter(Vector incident, Vector normal) {
		Vector random = Vector.uniformInHemisphere();
		Vector bvx = normal.getOrthagonal();
		Vector bvy = normal;
		Vector bvz = bvy.cross(bvx);
		return Vector.localToWorldCoords(random, bvx, bvy, bvz);
	}

	public boolean doDotProduct() {
		return true;
	}
	
}
