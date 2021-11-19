package com.JavaPathtracer.geometry;

public class Hit {

	public static final Hit MISS = new Hit();
	
	public boolean hit;
	public Vector point; // location of intersection
	public Vector normal; // normal vector 
	public Vector tangent; // bitangent can be calculated from the normal and the tangent
	public double distance; // distance from hitpoint to ray origin
	public Vector textureCoord;
	public Ray ray;
	
	private Hit() {
		this.hit = false;
		this.distance = Double.POSITIVE_INFINITY;
	}
	
	public Hit(boolean hit, Ray ray, Vector point, Vector normal, Vector tangent, double distance, Vector textureCoord) {
		this.hit = hit;
		this.ray = ray;
		this.point = point;
		this.normal = normal;
		this.tangent = tangent;
		this.distance = distance;
		this.textureCoord = textureCoord;
	}
	
	public Hit(Ray ray, Vector point, Vector normal, Vector tangent, double distance, Vector textureCoord) {
		this(true, ray, point, normal, tangent, distance, textureCoord);
	}

	public Hit(Hit hit) {
		this(hit.hit, hit.ray, hit.point, hit.normal, hit.tangent, hit.distance, hit.textureCoord);
	}
	
}
