package com.JavaPathtracer;

import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.Scene;

public class Pathtracer implements Raytracer {

	public static final double EPSILON = 0.000001;
	public static final double AIR_IOR = 1.0;
	
	// Maximum depth of bounces
	private int maxLightBounces;
	private static final Vector BLACK = new Vector();
	
	public Pathtracer(int maxLightBounces) {
		this.maxLightBounces = maxLightBounces;
	}

	// trace a ray
	public Vector pathtraceRay(Scene scene, Ray ray, int bounces, double ior) {

		if (bounces >= this.maxLightBounces) {
			return Vector.ZERO;
		}
		
		ObjectHit hit = scene.traceRay(ray);
		if (hit.hit) {

			Material mat = hit.material;
			/*if(mat instanceof EmissiveMaterial && !lights) {
				return BLACK;
			}*/
		
			return mat.shade(hit, bounces, scene, this, ior);
		
		} else {
			return scene.getSkyEmission(ray.direction);
		}

	}

	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		super.traceRay(scene, ray);
		return pathtraceRay(scene, ray, 0, true, AIR_IOR);
	}
	
	@Override
	public String toString() {
		return String.format("Pathtracer (%d light bounces)", this.maxLightBounces);
	}

}
