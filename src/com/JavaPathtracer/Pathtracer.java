package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.IMaterial;

public class Pathtracer extends Raytracer {

	// Maximum depth of bounces
	private int maxLightBounces;

	public Pathtracer(int maxLightBounces) {
		this.maxLightBounces = maxLightBounces;
	}

	// trace a ray
	public Vector pathtraceRay(Scene scene, Ray ray, int bounces) {

		if (bounces > this.maxLightBounces) {
			return new Vector(0.0, 0.0, 0.0);
		}

		Hit hit = scene.traceRay(ray);
		if (hit.hit) {
			IMaterial mat = hit.hitObject.getMaterial();
			return mat.shade(ray.direction, hit, bounces, scene, this);
		} else {
			return scene.getSkyEmission(ray.direction);
		}

	}

	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		return pathtraceRay(scene, ray, 0);
	}

}
