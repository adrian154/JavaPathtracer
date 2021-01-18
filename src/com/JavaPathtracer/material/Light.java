package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;

public class Light implements FiniteShape {

	protected Vector color;
	protected Sphere bounding;
	protected Shape shape;
	
	public Light(FiniteShape shape, Vector color) {
		this.shape = shape;
		this.bounding = shape.getBoundingSphere();
		this.color = color;
	}

	@Override
	public Hit intersect(Ray ray) {
		return shape.intersect(ray);
	}
	
	@Override
	public Sphere getBoundingSphere() {
		return bounding;
	}
	
}
