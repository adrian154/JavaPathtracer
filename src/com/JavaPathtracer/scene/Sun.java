package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

public class Sun {

	public double radius;
	public Vector direction;
	public Vector color;
	public double cosAngularRadius;
	
	// precompute angular radius to prevent two expensive calls to inverse trig funcs
	public Sun(Vector direction, double radius, Vector color) {
		this.direction = direction.normalize();
		this.radius = radius;
		this.color = color;
		this.cosAngularRadius = Math.cos(Math.atan(radius));
	}
	
}
