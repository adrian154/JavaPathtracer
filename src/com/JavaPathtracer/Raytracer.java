package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public interface Raytracer {
	public Vector traceRay(Scene scene, Ray ray);
}
