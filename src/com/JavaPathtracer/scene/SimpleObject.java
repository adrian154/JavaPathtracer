package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.material.Material;

public class SimpleObject implements WorldObject {

	public final Shape shape;
	public Material material;

	public SimpleObject(Shape shape, Material material) {
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
	@Override
	public ObjectHit traceRay(Ray ray) {
		Hit hit = shape.raytrace(ray);
		return new ObjectHit(hit, material);
	}

}