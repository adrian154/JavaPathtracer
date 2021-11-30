package com.JavaPathtracer.geometry;

// Geometry-only primitive
public interface Shape {
	
	public Hit raytrace(Ray ray);
	public BoundingBox getBoundingBox();
	public Vector pickRandomPoint();
	
}
