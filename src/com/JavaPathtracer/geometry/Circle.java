package com.JavaPathtracer.geometry;

public class Circle extends Plane implements Shape {

	public double radius;
	
	public Circle(Vector normal, Vector point, double radius) {
		super(normal, point);
		this.radius = radius;
	}
	
	public Hit intersect(Ray ray) {
		
		Hit hit = super.intersect(ray);
		if(hit.hit && hit.point.minus(this.point).lengthSquared() < this.radius * this.radius) {
			return hit;
		} else {
			return Hit.MISS;
		}
		
	}
	
	public Sphere getBoundingSphere() {
		return new Sphere(this.point, this.radius);
	}
	
}
