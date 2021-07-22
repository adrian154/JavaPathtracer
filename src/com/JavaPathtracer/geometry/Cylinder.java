package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Raytracer;

public class Cylinder implements Shape {

	private Vector point;
	private Vector direction;
	private double length;
	private double radius;
	
	public Cylinder(Vector point, Vector direction, double radius, double length) {
		this.point = point;
		this.direction = direction;
		this.radius = radius;
		this.length = length;
	}
	
	@Override
	public Hit intersect(Ray ray) {
		
		// horrible homebrewed isect code
		// featuring: poor understanding of vector geometry
		
		Vector point = this.point.minus(ray.origin);
		
		// mysterious magic numbers ahead
		double dcd = direction.dot(ray.direction);
		double dcp = direction.dot(point);
		double a = 1 - dcd;
		double b = -2 * point.dot(ray.direction) + 2 * dcd * dcp;
		double c = point.dot(point) - dcp * dcp - radius * radius;
		
		// quadratic solving from Sphere#intersect()
		double discrim = b * b - 4 * a * c;
		if (discrim < 0) {
			return null;
		}

		// im undergoing immense pain
		discrim = Math.sqrt(discrim);
		double t1 = (-b - discrim) / (2 * a);
		double t2 = (-b + discrim) / (2 * a);
		double t = 0;
		if(t1 < Raytracer.EPSILON && t2 < Raytracer.EPSILON) return null;
		if(t2 < Raytracer.EPSILON) t = t1;
		if(t1 < Raytracer.EPSILON) t = t2;
		t = Math.min(t1, t2);
		
		Hit result;
		Vector hitPoint = ray.getPoint(t);
		Vector normal = new Vector(1,0,0); // DUMMY
	
		if(normal.dot(ray.direction) > 0) {
			normal.invert();
		}
		
		// fuck you and your texture mapping
		result = new Hit(ray, hitPoint, normal, t, Vector.ZERO);
		return result;
		
	}
	
}
