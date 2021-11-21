package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Transform;
import com.JavaPathtracer.material.Material;

public class SimpleObject extends WorldObject {

	public final Shape shape;
	public Material material;

	public SimpleObject(Shape shape, Material material, Transform transform) {
		super(transform);
		this.shape = shape;
		this.material = material;
	}
	
	public SimpleObject(Shape shape, Material material) {
		this(shape, material, null);
	}

	public Shape getShape() {
		return shape;
	}

	public Material getMaterial() {
		return material;
	}

	public BoundingBox getBoundingBox() {
		return shape.getBoundingBox();
	}
	
	@Override
	public ObjectHit raytraceObject(Ray ray) {
		Hit hit = shape.raytrace(ray);
		return new ObjectHit(hit, this, material);
	}

}