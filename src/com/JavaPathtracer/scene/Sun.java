package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

public class Sun {

	public double radius;
	public Vector direction;
	public Vector color;
	
	public Sun(Vector direction, double radius, Vector color) {
		this.direction = direction;
		this.radius = radius;
		this.color = color;
	}
	
}
