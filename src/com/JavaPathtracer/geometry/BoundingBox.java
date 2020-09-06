package com.JavaPathtracer.geometry;

// container class for geometry
public class BoundingBox {

	public Vector min;
	public Vector max;
	
	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}

	public double area() {
		double width = max.x - min.x;
		double height = max.y - min.y;
		double depth = max.z - min.z;
		return 2 * (width * height + width * depth + height * depth);
	}
	
	public double volume() {
		return (max.x - min.x) * (max.y - min.y) * (max.z - min.z);
	}
	
}