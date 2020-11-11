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
			double amt = ray.origin.minus(hit.point).normalized().dot(hit.normal);
			return hit.hitObject.getMaterial().getColor(hit.textureCoordinates.x, hit.textureCoordinates.y).times(amt < 0 ? 0 : amt);
		} else {
			return scene.getSkyEmission(ray.direction);
		}
		
	}
	
}
