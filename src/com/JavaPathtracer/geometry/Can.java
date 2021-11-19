package com.JavaPathtracer.geometry;

// TODO: Come up with new name
public class Can implements Shape {

	private Circle circle;
	private Cylinder can;

	public Can(Vector direction, Vector point, double radius, double length) {
		this.circle = new Circle(direction, point, radius, true);
		this.can = new Cylinder(point, direction, radius, length);
	}

	@Override
	public Hit raytrace(Ray ray) {
		if(can.raytrace(ray).hit) return Hit.MISS;
		return circle.raytrace(ray);
	}

	@Override
	public BoundingBox getBoundingBox() {
		return circle.getBoundingBox();
	}

}
