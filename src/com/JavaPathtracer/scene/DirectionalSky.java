package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

public class DirectionalSky implements Environment {

	protected Vector direction;
	protected Vector colorA;
	protected Vector colorB;
	
	public DirectionalSky(Vector direction, Vector colorA, Vector colorB) {
		this.direction = direction.normalize();
		this.colorA = colorA;
		this.colorB = colorB;
	}
	
	public DirectionalSky(Vector direction, Vector color) {
		this(direction, color, Vector.ZERO);
	}
	
	public Vector getEmission(Vector inDir) {
		return inDir.dot(this.direction) > 0 ? colorA : colorB;
	}
	
}
