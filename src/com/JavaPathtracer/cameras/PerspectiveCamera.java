package com.JavaPathtracer.cameras;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class PerspectiveCamera extends Camera {

	private double focalLength;
	
	public void setFOV(double FOV) {
		this.focalLength = 0.5 / Math.tan(FOV * Math.PI / 360);
	}
	
	public double getFocalLength() {
		return this.focalLength;
	}
	
	@Override
	public Ray getCameraRay(double imagePlaneX, double imagePlaneY)  {
		Vector direction = new Vector(imagePlaneX, imagePlaneY, focalLength).normalized();
		Vector basis = up.cross(lookingAt);
		direction = Vector.localToWorldCoords(direction, basis, up, lookingAt);
		Ray result = new Ray(this.position, direction);
		return result;
	}
	
}
