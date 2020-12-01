package com.JavaPathtracer.geometry;

public class Ray {

	public Vector origin; // Origin of the ray
	public Vector direction; // Normalized direction vector

	public Ray(Vector origin, Vector direction) {
		this.origin = origin;
		this.direction = direction;
	}

	// Get point on ray based on distance from origin
	public Vector getPoint(double distance) {
		return origin.plus(direction.times(distance));
	}

}
