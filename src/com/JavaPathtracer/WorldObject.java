package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.material.Material;

public class WorldObject {

	protected Shape shape;
	protected Material material;

	public WorldObject(Shape shape, Material material) {
		this.shape = shape;
		this.material = material;
	}

	public Shape getShape() {
		return shape;
	}

	public Material getMaterial() {
		return material;
	}

	// do geometry+material raytrace
	public Hit traceRay(Ray ray) {
		Hit hit = shape.intersect(ray);
		hit.hitObject = this;
		return hit;
	}

}