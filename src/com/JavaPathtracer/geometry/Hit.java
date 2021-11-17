package com.JavaPathtracer.geometry;

public class Hit {

	public final Vector point; // location of intersection
	public final Vector normal; // normal vector 
	public final Vector tangent; // bitangent can be calculated from the normal and the tangent
	public final double distance; // distance from hitpoint to ray origin
	public final Vector textureCoordinates;
	public final Ray ray;
	
	// If you supply arguments it is assumed that there was an intersection
	public Hit(Ray ray, Vector point, Vector normal, Vector tangent, double distance, Vector textureCoordinates) {
		this.ray = ray;
		this.point = point;
		this.normal = normal;
		this.tangent = tangent;
		this.distance = distance;
		this.textureCoordinates = textureCoordinates;
	}

}
