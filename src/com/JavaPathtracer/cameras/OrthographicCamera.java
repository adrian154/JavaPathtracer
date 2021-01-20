package com.JavaPathtracer.cameras;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class OrthographicCamera extends Camera {

	private double scale;
	
	public void setScale(double scale) {
		this.scale = scale;
	}
	
	@Override
	public Ray getCameraRay(double imagePlaneX, double imagePlaneY) {
		Vector right = up.cross(lookingAt);
		return new Ray(this.position.plus(right.imul(imagePlaneX * scale)).iadd(up.times(imagePlaneY * scale)), this.lookingAt);
	}
	
}
