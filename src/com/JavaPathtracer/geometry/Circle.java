package com.JavaPathtracer.geometry;

import java.util.concurrent.ThreadLocalRandom;

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
	public Vector pickRandomPoint() {
		double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
		double length = ThreadLocalRandom.current().nextDouble() * radius;
		Vector offset = new Vector(Math.cos(angle) * length, 0, Math.sin(angle) * length);
		return this.point.plus(offset.fromCoordinateSpace(this.tangent, this.normal, this.tangent.cross(this.normal)));
	}

}
