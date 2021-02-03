package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public abstract class Raytracer {

	public static final double EPSILON = 0.00001;
	protected int rays;
	
	public Raytracer() {
		rays = 0;
	}
	
	public Vector traceRay(Scene scene, Ray ray) {
		rays++;
		return null;
	}
	
	public int getRays() {
		return rays;
	}
	
	public abstract String getName();

}
