package com.JavaPathtracer.geometry;

public class Circle extends Plane implements FiniteShape {

	public double radius;

	public Circle(Vector normal, Vector point, double radius) {
		super(normal, point);
		this.radius = radius;
	}

	@Override
	public Hit intersect(Ray ray) {

		Hit hit = super.intersect(ray);
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
