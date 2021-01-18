package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public abstract class Raytracer {

	public static final double EPSILON = 0.000001;
	public abstract Vector traceRay(Scene scene, Ray ray);

}
