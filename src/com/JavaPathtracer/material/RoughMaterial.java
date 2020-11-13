package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class RoughMaterial extends BaseMaterial {

	private SampleableScalar roughness;
	
	public RoughMaterial(ISampleable color, SampleableScalar roughness) {
		super(color);
		this.roughness = roughness;
	}
	
	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Pathtracer pathtracer) {
		
		Vector reflect = incident.minus(hit.normal.times(2 * hit.normal.dot(incident)));
		reflect.iadd(Vector.uniformInSphere().times(Math.random() * roughness.sampleScalar(hit.textureCoordinates.x, hit.textureCoordinates.y))).normalize();
		
		Ray next = new Ray(hit.point, reflect);
		return pathtracer.pathtraceRay(next, bounces + 1).times(this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y));
		
	}
	
}
