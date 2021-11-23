package com.JavaPathtracer;

import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.Scene;

public class Pathtracer implements Raytracer {

	public static final double EPSILON = 0.000001;
	public static final double AIR_IOR = 1.0;
	
	// Maximum depth of bounces
	private int maxLightBounces;
	
	public Pathtracer(int maxLightBounces) {
		this.maxLightBounces = maxLightBounces;
	}

	// trace a ray
	public Vector pathtraceRay(Scene scene, Ray ray, int bounces, double ior, boolean excludeLights) {

		if (bounces >= this.maxLightBounces) {
			return Vector.ZERO;
		}
		
		ObjectHit hit = scene.traceRay(ray, excludeLights);
		if (hit.hit) {
			if(scene.getLights().contains(hit.object) && excludeLights) return Vector.ZERO;
			try {
			return hit.material.shade(hit, bounces, scene, this, ior);
			} catch(Throwable t) {
				System.out.println(hit.object);
				throw t;
			}
		} else {
			return scene.getSky().getEmission(ray.direction);
		}

	}
	
	public Vector pathtraceRay(Scene scene, Ray ray, int bounces, double ior) {
		return this.pathtraceRay(scene, ray, bounces, ior, false);
	}

	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		return this.pathtraceRay(scene, ray, 0, AIR_IOR);
	}
	
	@Override
	public String toString() {
		return String.format("Pathtracer (%d light bounces)", this.maxLightBounces);
	}

}
