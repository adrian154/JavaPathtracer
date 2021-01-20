package com.JavaPathtracer.geometry;

import com.JavaPathtracer.scene.WorldObject;

public class Hit {

	public boolean hit; // Was there an intersection?
	public Vector point; // Point of intersection
	public Vector normal; // Point of normal
	public double distance; // Distance from hitpoint to ray origin
	public Vector textureCoordinates; // UV of the hit
	public WorldObject hitObject; // Hit object (used in tragically un-OOP ways!!)

	public static final Hit MISS = new Hit(false, null, null, Double.POSITIVE_INFINITY, null);
	public static final Hit BOGUS = new Hit(true, new Vector(1.0, 0.0, 0.0), new Vector(1.0, 0.0, 0.0), 0.0,
			new Vector(1.0, 0.0, 0.0));

	// If you supply arguments it is assumed that there was an intersection
	public Hit(boolean hit, Vector point, Vector normal, double distance, Vector textureCoordinates) {
		this.hit = hit;
		this.point = point;
		this.normal = normal;
		this.distance = distance;
		this.textureCoordinates = textureCoordinates;
	}

	public Hit(Vector point, Vector normal, double distance, Vector textureCoordinates) {
		this(true, point, normal, distance, textureCoordinates);
	}

}
