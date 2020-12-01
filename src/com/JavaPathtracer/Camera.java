package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class Camera {

	public Vector position; // Position of the camera
	public Vector lookingAt; // Unit vector, camera direction
	public Vector up; // Up vector (basis)
	private double focalLength;

	public Camera() {
		this.position = new Vector(0.0, 0.0, 0.0);
		this.lookingAt = new Vector(0.0, 0.0, 1.0);
		this.up = new Vector(0.0, 1.0, 0.0);
	}

	public Camera(Vector position) {
		this.position = position;
		this.lookingAt = new Vector(0.0, 0.0, 1.0);
		this.up = new Vector(0.0, 1.0, 0.0);
	}

	public Camera(Vector position, Vector lookingAt, Vector up) {
		this.position = position;
		this.lookingAt = lookingAt;
		this.up = up;
	}

	public void setFOV(double FOV) {
		this.focalLength = 0.5 / Math.tan(FOV * Math.PI / 360);
	}

	public void lookAt(Vector pos) {
		this.lookingAt = pos.minus(this.position).normalized();
		this.up = Vector.fromSpherical(this.lookingAt.toSpherical().minus(new Vector(0, Math.PI / 2, 0)));
	}

	public double getFocalLength() {
		return this.focalLength;
	}

	// takes image plane coordinates (-1 to 1) and returns a camera ray
	public Ray getCameraRay(double imagePlaneX, double imagePlaneY) {
		Vector direction = new Vector(imagePlaneX, imagePlaneY, focalLength).normalized();
		Vector basis = up.cross(lookingAt);
		direction = Vector.localToWorldCoords(direction, basis, up, lookingAt);
		Ray result = new Ray(this.position, direction);
		return result;
	}

}
