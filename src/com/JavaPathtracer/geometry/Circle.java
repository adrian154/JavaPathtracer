package com.JavaPathtracer.geometry;

public class Circle extends Plane implements FiniteShape {

	public double radius;

	public Circle(Vector normal, Vector point, double radius) {
		this(normal, point, radius, false);
	}
	
	public Circle(Vector normal, Vector point, double radius, boolean oneSided) {
		super(normal, point, oneSided);
		this.radius = radius;
	}

	@Override
	public Hit raytrace(Ray ray) {

		Hit hit = super.raytrace(ray);
		if (hit != null && hit.point.minus(this.point).lengthSquared() < this.radius * this.radius) {
			return hit;
		} else {
			return null;
		}

	}

	public Sphere getBoundingSphere() {
		return new Sphere(this.point, this.radius);
	}

}
