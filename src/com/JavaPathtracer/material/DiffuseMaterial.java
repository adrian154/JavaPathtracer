package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class DiffuseMaterial extends BaseMaterial {
	
	public DiffuseMaterial(ISampleable color) {
		super(color);
	}
	
	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Pathtracer pathtracer) {
		
		Vector random = Vector.uniformInHemisphere();
		Vector bvx = hit.normal.getOrthagonal();
		Vector bvy = hit.normal;
		Vector bvz = bvy.cross(bvx);
		
		Ray next = new Ray(hit.point, Vector.localToWorldCoords(random, bvx, bvy, bvz));
		
		Vector color = this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
		double dot = incident.dot(hit.normal);
		return pathtracer.pathtraceRay(next, bounces + 1).times(color).times(dot);
		
	}
	
}
