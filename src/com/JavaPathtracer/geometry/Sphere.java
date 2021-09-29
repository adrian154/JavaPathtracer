package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Raytracer;

public class Sphere implements FiniteShape {

	private Vector center;
	private double radius;

	public Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Hit intersect(Ray ray) {

		Vector center = this.center.minus(ray.origin);

		double b = -2 * ray.direction.dot(center);
		double c = center.dot(center) - radius * radius;

		double discrim = b * b - 4 * c;

		if (discrim < 0) {
			return null;
		}

		discrim = Math.sqrt(discrim);
		double t = (-b - discrim) / 2;
		if (t < Raytracer.EPSILON) {
			t = (-b + discrim) / 2;
			if (t < Raytracer.EPSILON) {
				return null;
			}
		}

		Vector point = ray.getPoint(t);
		Vector normal = point.minus(this.center).normalized();

		if(normal.dot(ray.direction) > 0) {
			normal.invert();
		}
		
		return new Hit(ray, point, normal, t);
	
	}

	public Vector getCenter() {
		return this.center;
	}
	
	public double getRadius() {
		return this.radius;
	}
	
	public Sphere getBoundingSphere() {
		return this;
	}

}
