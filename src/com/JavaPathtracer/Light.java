package com.JavaPathtracer;

import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.scene.SimpleObject;

public class Light extends SimpleObject implements FiniteShape {

	public EmissiveMaterial material;
	protected Sphere bounding;
	
	public Light(FiniteShape shape, EmissiveMaterial material) {
		super(shape, null);
		this.bounding = shape.getBoundingSphere();
		this.material = material;
	}

	@Override
	public Hit intersect(Ray ray) {
		Hit hit = shape.intersect(ray);
		if(hit != null) hit.material = material;
		return hit;
	}
	
	@Override
	public Sphere getBoundingSphere() {
		return bounding;
	}
	
}
