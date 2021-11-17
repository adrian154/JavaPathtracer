package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.material.Material;

public class SimpleObject implements WorldObject {

	protected Shape shape;
	protected Material material;

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
	public Hit traceRay(Ray ray) {
		Hit hit = shape.raytrace(ray);
		if(hit != null)
			hit.material = material;
		return hit;
	}

}