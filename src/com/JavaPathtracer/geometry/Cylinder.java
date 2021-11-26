package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Pathtracer;

public class Cylinder implements Shape {

	private Vector point, direction;
	private double length;
	private double radius;
	
	private Transform transform;
	
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
		this.transform = new TransformBuilder().translate(point).toCoordinateSpace(bvx, bvy, bvz).build();
		System.out.println(this.transform);
	}
	
	// TODO
	@Override
	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		// TL;DR: perform intersection with a cylinder aligned along (0, 1, 0) to make transforms easier
		Vector d = transform.inverseVector(ray.direction);
		Vector o = transform.inversePoint(ray.origin);
		
		// solve for `t` such that (x, z) is `radius` units from the y-axis
		double a = d.x * d.x + d.z * d.z;
		double b = 2 * (o.x * d.x + o.z * d.z);
		double c = o.x * o.x + o.z * o.z - radius * radius;
		
		double discrim = b * b - 4 * a * c;
		if(discrim < 0) return Hit.MISS; // no solutions, i.e. the ray is parallel to the cylinder

		// t1 will always be closer (discrim > 0 and a > 0)
		discrim = Math.sqrt(discrim);
		double t1 = (-b - discrim) / (2 * a);
		double t2 = (-b + discrim) / (2 * a);

		if(t1 < Pathtracer.EPSILON && t2 < Pathtracer.EPSILON) return Hit.MISS;
		
		double t;
		if(t1 < Pathtracer.EPSILON) {
			t = t2;
		} else if(t2 < Pathtracer.EPSILON) {
			t = t1;
		} else {
			t = Math.min(t1, t2);
		}

		Vector hitLocal = o.plus(d.times(t));
		
		// reject hits beyond the length of the cylinder
		if(hitLocal.y < 0 || hitLocal.y > length) return Hit.MISS;
		Vector normalLocal = new Vector(hitLocal.x, 0, hitLocal.z).normalize();
		
		// transform back everything
		Vector point = transform.transformPoint(hitLocal);
		Vector normal = transform.transformNormal(normalLocal).facing(ray.direction);
		
		// TODO: texture mapping for cylinder
		// TODO: tangent vector
		return new Hit(ray, point, normal, null, t, Vector.ZERO);
		
	}
	
}
