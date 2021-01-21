package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public class DebugTracer2 extends Raytracer {
	
	private Vector shadeNormal(Vector hit) {
		return hit.plus(Vector.ONE).idiv(2);
	}
	
	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		
		super.traceRay(scene, ray);

		Hit hit = scene.traceRay(ray);
		if (hit.hit) {
			return shadeNormal(hit.normal);
		} else {
			return Vector.ZERO;
		}

	}

}
