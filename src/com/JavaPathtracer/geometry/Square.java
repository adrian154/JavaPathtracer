package com.JavaPathtracer.geometry;

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
		if (hit != null && Math.abs(hit.point.x - point.x) < size && Math.abs(hit.point.z - point.z) < size) {
			return hit;
		}

		return null;

	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return new BoundingBox(point.plus(-size), point.plus(size)); // degenerate bounding box, but that's okay
	}

}
