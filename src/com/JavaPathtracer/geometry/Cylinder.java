package com.JavaPathtracer.geometry;

import javax.lang.model.util.Elements.Origin;

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
		
		// life is only hard if you make it hard
		
		// be the chance that you want to see
		// y basis vector = direction
		Vector bvx = direction.getOrthagonal();
		Vector bvz = direction.cross(bvx);
		
		Vector d = Vector.localToWorldCoords(ray.direction, bvx, direction, bvz);
		Vector o = Vector.localToWorldCoords(ray.origin.minus(point), bvx, direction, bvz);
		
		double a = d.x * d.x + d.z * d.z;
		double b = 2 * (o.x * d.x + o.z * d.z);
		double c = o.x * o.x + o.z * o.z - radius * radius;
		
		double discrim = b * b - 4 * a * c;
		if(discrim < 0) return null;
		
		discrim = Math.sqrt(discrim);
		
		double t1 = (-b - discrim) / (2 * a);
		double t2 = (-b + discrim) / (2 * a);

		if(t1 < Raytracer.EPSILON && t2 < Raytracer.EPSILON) return null;
		
		Vector hitLocal;
		Vector hit1 = o.plus(d.times(t1));
		Vector hit2 = o.plus(d.times(t2));
		
		if(t1 < Raytracer.EPSILON) {
			hitLocal = hit2;
		} else if(t2 < Raytracer.EPSILON) {
			hitLocal = hit1;
		} else {
			if(hit1.y > length || hit1.y < 0) hit1 = null;
			if(hit2.y > length || hit2.y < 0) hit2 = null;
			hitLocal = hit1 == null ? hit2 : hit1;
		}
		
		// transform back normal
		if(hitLocal == null) return null;
		Vector normalLocal = new Vector(hitLocal.x, 0, hitLocal.z).normalize();
		
		Vector point = Vector.localToWorldCoords(hitLocal, bvx, direction, bvz);
		Vector normal = Vector.localToWorldCoords(normalLocal, bvx, direction, bvz);
		
		// TODO: texture mapping for cylinder
		return new Hit(ray, point, normal, t1, new Vector(0.5, 0.5, 0.0));
		
	}
	
}
