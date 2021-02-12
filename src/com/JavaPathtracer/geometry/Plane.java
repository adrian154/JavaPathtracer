package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Raytracer;

public class Plane implements Shape {

	public Vector normal;
	public Vector point;
	public double tilingSize;

	public Plane(Vector normal, Vector point) {
		this(normal, point, 1.0);
	}

	public Plane(Vector normal, Vector point, double tilingSize) {
		this.normal = normal.normalize();
		this.point = point;
		this.tilingSize = tilingSize;
	}

	/*
	 * Ray-plane intersection.
	 */
	@Override
	public Hit intersect(Ray ray) {

		// reject rays that are wonky (wrong side!))
		//if(ray.direction.dot(this.normal) > 0) return Hit.MISS;
		
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
		Vector vec = point.minus(this.point);
		Vector ortho = normal.getOrthagonal();
		double u = ortho.dot(vec);
		double v = normal.cross(ortho).dot(vec);
		u = u - Math.floor(u / this.tilingSize) * this.tilingSize;
		v = v - Math.floor(v / this.tilingSize) * this.tilingSize;
		
		return new Hit(ray, point, normal, distance, new Vector(u / this.tilingSize, v / this.tilingSize, 0.0));

	}

}
