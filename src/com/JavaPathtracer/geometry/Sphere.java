package com.JavaPathtracer.geometry;

import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.Pathtracer;

public class Sphere implements Shape {

	public Vector center;
	public double radius;
	
	public Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	@Override
	public Vector pickRandomPoint() {
		return Vector.fromSpherical(ThreadLocalRandom.current().nextDouble() * 2 * Math.PI, Math.acos(ThreadLocalRandom.current().nextDouble())).times(radius).plus(center);
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(center.plus(-radius), center.plus(radius));
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		Vector center = this.center.minus(ray.origin);

		double b = -2 * ray.direction.dot(center);
		double c = center.dot(center) - radius * radius;

		double discrim = b * b - 4 * c;

		if (discrim < 0) {
			return Hit.MISS;
		}

		discrim = Math.sqrt(discrim);
		double t = (-b - discrim) / 2;
		if (t < Pathtracer.EPSILON) {
			t = (-b + discrim) / 2;
			if (t < Pathtracer.EPSILON) {
				return Hit.MISS;
			}
		}

		Vector point = ray.getPoint(t);
		Vector normal = point.minus(this.center).normalize();

		if(normal.dot(ray.direction) > 0) {
			normal = normal.reverse();
		}
		
		// Do texture mapping
		Vector invDir = new Vector(0.0, 0.0, 0.0).minus(normal);
		double u = 0.5 + Math.atan2(invDir.z, invDir.x) / (2 * Math.PI);
		double v = 0.5 + Math.asin(invDir.y) / Math.PI;

		// TODO: tangent vector
		return new Hit(ray, point, normal, null, t, new Vector(u, v, 0.0));
	
	}

}
