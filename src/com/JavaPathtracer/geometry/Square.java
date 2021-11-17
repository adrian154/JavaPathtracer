package com.JavaPathtracer.geometry;

// this thing extends circle because i don't want to make a FinitePlane class
public class Square extends Plane implements FiniteShape {

	private double size;
	
	public Square(Vector normal, Vector point, double size) {
		this(normal, point, size, false);
	}

	public Square(Vector normal, Vector point, double size, boolean oneSided) {
		this(normal, point, size, 1.0, oneSided);
	}
	
	public Square(Vector normal, Vector point, double size, double tilingSize) {
		this(normal, point, size, tilingSize, false);
	}
	
	public Square(Vector normal, Vector point, double size, double tilingSize, boolean oneSided) {
		super(normal, point, tilingSize, oneSided);
		this.size = size;
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		Hit hit = super.raytrace(ray);
		if (hit != null && Math.abs(hit.point.x - point.x) < size && Math.abs(hit.point.z - point.z) < size) {
			return hit;
		}

		return null;

	}
	
	@Override
	public Sphere getBoundingSphere() {
		return new Sphere(this.point, this.size);
	}

}
