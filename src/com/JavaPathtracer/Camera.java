package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Transforms;
import com.JavaPathtracer.geometry.Vector;

public class Camera {

	private Vector position;		// Position of the camera
	private Vector lookingAt;		// Unit vector, camera direction
	private Vector up;				// Up vector (basis)
	
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
	
	// takes image plane coordinates (-1 to 1) and returns a camera ray
	public Ray getCameraRay(double imagePlaneX, double imagePlaneY) {
		Vector direction = new Vector(imagePlaneX, imagePlaneY, 1.0).normalized();
		Vector basis = up.cross(lookingAt);
		direction = Transforms.localToWorldCoords(direction, basis, up, lookingAt);
		Ray result = new Ray(this.position, direction);
		return result;
	}
	
}
