package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.material.IMaterial;

public class WorldObject {

	protected Shape shape;
	protected IMaterial material;

	public WorldObject(Shape shape, IMaterial material) {
		this.shape = shape;
		this.material = material;
	}

	public Shape getShape() {
		return shape;
	}

	public IMaterial getMaterial() {
		return material;
	}

	// do geometry+material raytrace
	public Hit traceRay(Ray ray) {
		Hit hit = shape.intersect(ray);
		hit.hitObject = this;
		return hit;
	}

}