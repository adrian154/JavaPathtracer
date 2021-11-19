package com.JavaPathtracer.cameras;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public abstract class Camera {

	protected Vector position; // Position of the camera
	protected Vector lookingAt; // Unit vector, camera direction
	protected Vector up; // Up vector (basis)
	protected boolean jitter;
	
	public Camera() {
		this.position = new Vector(0.0, 0.0, 0.0);
		this.lookingAt = new Vector(0.0, 0.0, 1.0);
		this.up = new Vector(0.0, 1.0, 0.0);
		this.jitter = true;
	}

	// figure out other basis vector
	// lookAt() / setAngles() both assume the camera can only tilt along azimuth/inclination
	protected static Vector getUp(Vector look) {
		double length = Math.sqrt(look.x * look.x + look.z * look.z);
		return new Vector(-look.x / length * look.y, length, -look.z / length * look.y);
	}
	
	public void moveTo(Vector vector) {
		this.position = vector;
	}
	
	public void move(Vector vector) {
		this.position = this.position.plus(vector);
	}
	
	public void setAngles(double yaw, double pitch) {
		this.lookAt(Vector.fromSpherical(yaw, pitch));
	}
	
	public void lookAt(Vector direction) {
		this.lookingAt = direction;
		this.up = getUp(direction);
	}
	
	public void lookAtPoint(Vector point) {
		this.lookAt(point.minus(position).normalize());
	}
	
	public Vector getLook() {
		return this.lookingAt;
	}
	
	public Vector getPos() {
		return this.position;
	}
	
	public void enableJitter() { jitter = true; }
	public void disableJitter() { jitter = false; }

	// takes image plane coordinates (-1 to 1) and returns a camera ray
	public abstract Ray getCameraRay(double imagePlaneX, double imagePlaneY);
	
	public Ray getCameraRay(int x, int y, int maxDim, double jitterX, double jitterY) {
		double imageX = ((double)x / maxDim) * 2 - 1;
		double imageY = ((double)y / maxDim) * 2 - 1;
		if(jitter)
			return getCameraRay(imageX + (Math.random() - 0.5) * jitterX, imageY + (Math.random() - 0.5) * jitterY);
		else
			return getCameraRay(imageX, imageY);
	}
	
	@Override
	public String toString() {
		return String.format("position=%s, look=%s, up=%s", position.toString(), lookingAt.toString(), up.toString());
	}

}
