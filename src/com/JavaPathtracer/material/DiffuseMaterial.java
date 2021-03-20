package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public class DiffuseMaterial extends BRDFMaterial {

	public DiffuseMaterial(Sampleable color) {
		super(color);
	}
	
	public DiffuseMaterial() {
		super(new Vector(1.0, 1.0, 1.0));
	}
	
	@Override
	public double PDF(Vector incident, Vector outgoing, Vector normal) {
		return outgoing.dot(normal);
		//return 1;
	}
	
	@Override
	public Vector sample(Vector incident, Hit hit) {
		
		Vector random = Vector.fromSpherical(Math.random() * 2 * Math.PI, Math.random() * Math.PI / 2);
		//Vector random = Vector.uniformInHemisphere();
		Vector bvx = hit.normal.getOrthagonal();
		Vector bvy = hit.normal;
		Vector bvz = bvy.cross(bvx);

		return Vector.localToWorldCoords(random, bvx, bvy, bvz);
	
	}
	
	@Override
	public double BRDF(Vector incident, Vector outgoing, Vector normal) {
		return normal.dot(outgoing) < 0 ? 0 : 1;
	}
	
	@Override
	public boolean sampleLights() {
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Diffuse %s", color.toString());
	}

}
