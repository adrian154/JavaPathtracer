package com.JavaPathtracer.geometry;

public class Hit {

	public Ray ray;
	public Vector point; // Point of intersection
	public Vector normal; // Point of normal
	public double distance; // Distance from hitpoint to ray origin

	public Hit(Ray ray, Vector point, Vector normal, double distance) {
		this.ray = ray;
		this.point = point;
		this.normal = normal;
		this.distance = distance;
	}

}
