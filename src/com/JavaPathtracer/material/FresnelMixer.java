package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public class FresnelMixer implements Material {

	private Material A, B;
	private double reflectance;
	
	public FresnelMixer(Material A, Material B, double reflectance) {
		this.A = A;
		this.B = B;
	}
	
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double iorIn) {
		double NdotI = hit.normal.dot(hit.ray.direction);
		double a = 1 + NdotI;
		double actualReflectance = reflectance + (1 - reflectance) * a*a*a*a*a;
		return B.shade(hit, bounces, scene, pathtracer, iorIn).times(actualReflectance).plus(A.shade(hit, bounces, scene, pathtracer, iorIn).times(1 - actualReflectance));
	}
	
}
