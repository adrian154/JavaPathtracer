package com.JavaPathtracer.geometry;

// this thing extends circle because i don't want to make a FinitePlane class
public class Square extends Plane implements FiniteShape {

	private double size;
	
	public Square(Vector normal, Vector point, double size) {
		super(normal, point);
		this.size = size;
	}

	@Override
	public Hit intersect(Ray ray) {

		Hit hit = super.intersect(ray);
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
