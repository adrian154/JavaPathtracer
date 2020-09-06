package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class DebugTracer extends Raytracer {

	public DebugTracer(Camera camera, Scene scene) {
		super(camera, scene);
	}
	
	public Vector traceRay(Ray ray) {
		
		Hit hit = scene.traceRay(ray);
		if(hit.hit) {
			double amt = new Vector(0.0, 0.0, 0.0).minus(ray.direction).dot(hit.normal);
			return Raytracer.shadeNormal(hit.normal).times(amt);
		} else {
			return scene.getSkyEmission(ray.direction);
		}
		
	}
	
}
