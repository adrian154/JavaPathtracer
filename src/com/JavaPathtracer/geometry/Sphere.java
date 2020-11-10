package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Raytracer;

public class Sphere implements Shape {

	private Vector center;
	private double radius;
	
	public Sphere(Vector center, double radius) {
		this.center = center;
		this.radius = radius;
	}
	
	public Hit intersect(Ray ray) {
		
		Vector center = this.center.minus(ray.origin);
		
		double b = -2 * ray.direction.dot(center);
		double c = center.dot(center) - radius * radius;
		
		double discrim = b * b - 4 * c;
		
		if(discrim < 0) {
			return Hit.MISS;
		}
		
		discrim = Math.sqrt(discrim);
		double t = (-b - discrim) / 2;
		if(t < Raytracer.EPSILON) {
			t = (-b + discrim) / 2;
			if(t < Raytracer.EPSILON) {
				return Hit.MISS;
			}
		}
		
		Hit result;
		Vector point = ray.getPoint(t);
		Vector normal = point.minus(this.center).normalized();
		
		// Do texture mapping
		Vector invDir = new Vector(0.0, 0.0, 0.0).minus(normal);
		double u = 0.5 + Math.atan2(invDir.z, invDir.x) / (2 * Math.PI);
		double v = 0.5 - Math.asin(invDir.y) / Math.PI;
		
		result = new Hit(point, normal, t, new Vector(u, v, 0.0));
		
		return result;
		
	}
	
	public Sphere getBoundingSphere() {
		return this;
	}
	
}
