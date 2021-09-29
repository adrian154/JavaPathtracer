package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Raytracer;

public class Plane implements Shape {

	public Vector normal;
	public Vector point;

	public Plane(Vector normal, Vector point) {
		this.normal = normal;
		this.point = point;
	}
	

	/*
	 * Ray-plane intersection.
	 */
	@Override
	public Hit intersect(Ray ray) {
		
		/* Check if equation has solution. */
		double denom = ray.direction.dot(this.normal);
		if (denom == 0) {
			return null;
		}

		/* Solve for the distance. */
		double distance = this.point.minus(ray.origin).dot(this.normal) / denom;

		/* Make sure hit point is not behind ray. */
		if (distance < Raytracer.EPSILON) {
			return null;
		}

		Vector point = ray.getPoint(distance);

		return new Hit(ray, point, normal, distance);

	}

}
