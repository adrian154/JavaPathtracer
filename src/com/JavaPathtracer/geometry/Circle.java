package com.JavaPathtracer.geometry;

public class Circle extends Plane {

	public double radius;

	public Circle(Vector normal, Vector point, double radius) {
		this(normal, point, radius, false);
	}
	
	public Circle(Vector normal, Vector point, double radius, boolean oneSided) {
		super(normal, point, oneSided);
		this.radius = radius;
	}

	@Override
	public Hit raytrace(Ray ray) {

		Hit hit = super.raytrace(ray);
		if (hit.hit && hit.point.minus(this.point).lengthSquared() < this.radius * this.radius) {
			return hit;
		} else {
			return Hit.MISS;
		}

	}

	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(this.point.plus(-radius), this.point.plus(radius));
	}

}
