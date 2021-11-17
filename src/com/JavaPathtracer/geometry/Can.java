package com.JavaPathtracer.geometry;

// TODO: Come up with new name
public class Can implements FiniteShape {

	private Circle circle;
	private Cylinder can;

	public Can(Vector direction, Vector point, double radius, double length) {
		this.circle = new Circle(direction, point, radius, true);
		this.can = new Cylinder(point, direction, radius * 1.3, length);
	}

	@Override
	public Hit raytrace(Ray ray) {
		if(can.raytrace(ray) != null) return null;
		return circle.raytrace(ray);
	}

	public Sphere getBoundingSphere() {
		return circle.getBoundingSphere();
	}

}
