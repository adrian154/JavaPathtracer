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
			return Raytracer.shadeNormal(hit.normal);
		} else {
			return scene.getSkyEmission(ray.direction);
		}
		
	}
	
}
