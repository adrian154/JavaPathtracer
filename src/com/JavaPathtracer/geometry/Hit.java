package com.JavaPathtracer.geometry;

import com.JavaPathtracer.material.Material;

public class Hit {

	public Vector point; // Point of intersection
	public Vector normal; // Point of normal
	public double distance; // Distance from hitpoint to ray origin
	public Vector textureCoordinates; // UV of the hit
	public Ray ray;
	public Material material; // used in tragically un-OOP ways!
	
	// If you supply arguments it is assumed that there was an intersection
	public Hit(Ray ray, Vector point, Vector normal, double distance, Vector textureCoordinates) {
		this.ray = ray;
		this.point = point;
		this.normal = normal;
		this.distance = distance;
		this.textureCoordinates = textureCoordinates;
	}

}
