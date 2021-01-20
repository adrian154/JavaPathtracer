package com.JavaPathtracer.cameras;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public abstract class Camera {

	protected Vector position; // Position of the camera
	protected Vector lookingAt; // Unit vector, camera direction
	protected Vector up; // Up vector (basis)

	public Camera() {
		this.position = new Vector(0.0, 0.0, 0.0);
		this.lookingAt = new Vector(0.0, 0.0, 1.0);
		this.up = new Vector(0.0, 1.0, 0.0);
	}

	public void moveTo(Vector vector) {
		this.position = vector;
	}
	
	public void lookAt(Vector pos) {
		this.lookingAt = pos.minus(this.position).normalized();
		this.up = Vector.fromSpherical(this.lookingAt.toSpherical().minus(new Vector(0, Math.PI / 2, 0)));
	}
	
	public void setAngles(double yaw, double pitch) {
		this.lookingAt = Vector.fromSpherical(yaw, pitch);
		this.up = Vector.fromSpherical(yaw, pitch - Math.PI / 2);
	}

	// takes image plane coordinates (-1 to 1) and returns a camera ray
	public abstract Ray getCameraRay(double imagePlaneX, double imagePlaneY);
	
	public Ray getCameraRay(int x, int y, int maxDim, double jitterX, double jitterY) {
		double imageX = ((double)x / maxDim) * 2 - 1;
		double imageY = ((double)y / maxDim) * 2 - 1;
		return getCameraRay(imageX + (Math.random() - 0.5) * jitterX, imageY + (Math.random() - 0.5) * jitterY);
	}

}
