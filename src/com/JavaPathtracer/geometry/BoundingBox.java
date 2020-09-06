package com.JavaPathtracer.geometry;

// container class for geometry
public class BoundingBox implements Shape {

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

	public Hit intersect(Ray ray) {
		
		// Yet another micro-optimization!
		double invX = 1 / ray.direction.x;
		double invY = 1 / ray.direction.y;
		double invZ = 1 / ray.direction.z;
		
		double t1 = (min.x - ray.origin.x) * invX;
		double t2 = (max.x - ray.origin.x) * invX;
		
		double t3 = (min.y - ray.origin.y) * invY;
		double t4 = (max.y - ray.origin.y) * invY;
		
		double t5 = (min.z - ray.origin.z) * invZ;
		double t6 = (max.z - ray.origin.z) * invZ;
		
		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
		
		if(tmax < 0)
			return Hit.MISS;
		
		if(tmin > tmax)
			return Hit.MISS;
		
		// Everything on this Hit besides whether it hit is bogus
		// Beware!
		return new Hit(true, null, null, 0, null);
		
	}

	// For debugging purposes
	public String toString() {
		return "(min=" + this.min + ", max=" + this.max + ")";
	}
	
}