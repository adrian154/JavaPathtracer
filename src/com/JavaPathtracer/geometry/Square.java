package com.JavaPathtracer.geometry;

import java.util.concurrent.ThreadLocalRandom;

// this thing extends circle because i don't want to make a FinitePlane class
public class Square extends Plane {

	private double size;
	
	public Square(Vector normal, Vector point, double size) {
		this(normal, point, size, false);
	}

	public Square(Vector normal, Vector point, double size, boolean oneSided) {
		this(normal, point, size, 1.0, oneSided, normal.getOrthagonal());
	}
	
	public Square(Vector normal, Vector point, double size, double tilingSize) {
		this(normal, point, size, tilingSize, false, normal.getOrthagonal());
	}
	
	public Square(Vector normal, Vector point, double size, Vector tangent) {
		this(normal, point, size, 1.0, false, tangent);
	}
	
	public Square(Vector normal, Vector point, double size, double tilingSize, boolean oneSided, Vector tangent) {
		super(normal, point, tilingSize, oneSided, tangent);
		this.size = size;
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		Hit hit = super.raytrace(ray);
		if (hit.hit && Math.abs(hit.point.x - point.x) < size && Math.abs(hit.point.z - point.z) < size) {
			return hit;
		}

		return Hit.MISS;

	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(point.plus(-size), point.plus(size));
	}
	
	@Override
	public Vector pickRandomPoint() {
		Vector bitangent = this.tangent.cross(this.normal);
		return this.point.plus(this.tangent.times((ThreadLocalRandom.current().nextDouble() * 2 - 1) * size)).plus(bitangent.times((ThreadLocalRandom.current().nextDouble() * 2 - 1) * size));
	}

}
