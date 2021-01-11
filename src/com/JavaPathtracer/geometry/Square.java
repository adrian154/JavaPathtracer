package com.JavaPathtracer.geometry;

public class Square extends Plane {

	public double size;

	public Square(Vector normal, Vector point, double size) {
		super(normal, point);
		this.size = size;
	}

	@Override
	public Hit intersect(Ray ray) {

		Hit hit = super.intersect(ray);
		if (hit.hit && Math.abs(hit.point.x - point.x) < size && Math.abs(hit.point.z - point.z) < size) {
			return hit;
		}

		return Hit.MISS;

	}

}
