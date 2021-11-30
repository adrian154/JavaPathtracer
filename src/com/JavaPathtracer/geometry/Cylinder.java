package com.JavaPathtracer.geometry;

import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.Pathtracer;

public class Cylinder implements Shape {

	private Vector point, direction;
	private double length;
	private double radius;
	
	private Matrix3x3 matrix, inverse;
	
	public Cylinder(Vector point, Vector direction, double radius, double length) {
		this.point = point;
		this.direction = direction;
		this.radius = radius;
		this.length = length;
		this.updateTransform();
	}
	
	public void setPosition(Vector position) {
		this.point = position;
		this.updateTransform();
	}
	
	public void setDirection(Vector direction) {
		this.direction = direction;
		this.updateTransform();
	}
	
	private void updateTransform() {
		Vector bvy = direction, bvx = direction.getOrthagonal(), bvz = bvx.cross(bvy);
		this.matrix = new Matrix3x3(new double[] {
			bvx.x, bvy.x, bvz.x,
			bvx.y, bvy.y, bvz.y,
			bvx.z, bvy.z, bvz.z 
		});
		this.inverse = matrix.inverse();
	}
	
	// TODO
	@Override
	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Vector pickRandomPoint() {
		
		double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
		Vector offset = new Vector(Math.cos(angle) * this.radius, 0, Math.sin(angle) * this.radius);
	
		return this.point.plus(this.direction.times(ThreadLocalRandom.current().nextDouble() * this.length)) // move up the center of the cylinder
				         .plus(this.matrix.transform(offset)); // and then rotate to a random position along the circle                                          
	
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		// TL;DR: perform intersection with a cylinder aligned along (0, 1, 0) to make transforms easier
		Vector d = this.inverse.transform(ray.direction);
		Vector o = this.inverse.transform(ray.origin.minus(point));
		
		// solve for `t` such that (x, z) is `radius` units from the y-axis
		double a = d.x * d.x + d.z * d.z;
		double b = 2 * (o.x * d.x + o.z * d.z);
		double c = o.x * o.x + o.z * o.z - radius * radius;
		
		double discrim = b * b - 4 * a * c;
		if(discrim < 0) return Hit.MISS; // no solutions, i.e. the ray is parallel to the cylinder

		// there may be up to two intersection points
		discrim = Math.sqrt(discrim);
		double t1 = (-b - discrim) / (2 * a);
		double t2 = (-b + discrim) / (2 * a);

		// if both are behind the camera, we're done
		if(t1 < Pathtracer.EPSILON && t2 < Pathtracer.EPSILON) return Hit.MISS;
		
		Vector hitLocal;
		double t;
		Vector hit1 = o.plus(d.times(t1));
		Vector hit2 = o.plus(d.times(t2));
		
		if(t1 < Pathtracer.EPSILON) {
			if(hit2.y > length || hit2.y < 0) {
				return Hit.MISS;
			} else {
				hitLocal = hit2;
				t = t2;
			}
		} else {
			if(hit1.y > length || hit1.y < 0) {
				return Hit.MISS;
			} else {
				hitLocal = hit1;
				t = t1;
			}
		}
		
		// reject hits beyond the length of the cylinder
		Vector normalLocal = new Vector(hitLocal.x, 0, hitLocal.z).normalize();
		
		// transform back everything
		Vector point = this.matrix.transform(hitLocal).plus(this.point);
		Vector normal = this.matrix.transform(normalLocal).facing(ray.direction);
		
		// TODO: texture mapping for cylinder
		// TODO: tangent vector
		return new Hit(ray, point, normal, null, t, Vector.ZERO);
		
	}
	
}
