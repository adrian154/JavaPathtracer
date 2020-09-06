package com.JavaPathtracer.geometry;

// Geometry-only primitive
public interface Shape {

	public Hit intersect(Ray ray);
	
}
