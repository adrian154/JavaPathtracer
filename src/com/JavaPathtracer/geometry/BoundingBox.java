package com.JavaPathtracer.geometry;

// container class for geometry
public class BoundingBox {

	public Vector min;
	public Vector max;
	
	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}
	
}
