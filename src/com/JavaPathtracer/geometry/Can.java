package com.JavaPathtracer.geometry;

// TODO: Come up with new name
public class Can extends Circle {

	private Cylinder can;

	public Can(Vector direction, Vector point, double radius, double length) {
		super(direction, point, radius);
		this.can = new Cylinder(point, direction, radius, length);
	}

	@Override
	public Hit raytrace(Ray ray) {
		if(can.raytrace(ray).hit) return Hit.MISS;
		return super.raytrace(ray);
	}

}
