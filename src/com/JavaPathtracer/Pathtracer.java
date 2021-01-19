package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.IMaterial;

public class Pathtracer extends Raytracer {

	// Maximum depth of bounces
	private int maxLightBounces;
	private static final Vector BLACK = new Vector();
	
	public Pathtracer(int maxLightBounces) {
		this.maxLightBounces = maxLightBounces;
	}

	// trace a ray
	public Vector pathtraceRay(Scene scene, Ray ray, int bounces, boolean lights) {

		if (bounces >= this.maxLightBounces) {
			return new Vector(0.0, 0.0, 0.0);
		}
		
		Hit hit = scene.traceRay(ray);
		if (hit.hit) {
			
			IMaterial mat = hit.hitObject.getMaterial();
			if(mat instanceof EmissiveMaterial && !lights) {
				return BLACK;
			}
		
			return mat.shade(ray.direction, hit, bounces, scene, this);
		
		} else {
			return scene.getSkyEmission(ray.direction);
		}

	}

	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		super.traceRay(scene, ray);
		return pathtraceRay(scene, ray, 0, true);
	}

}
