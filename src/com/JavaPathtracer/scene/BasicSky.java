package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

public class BasicSky implements Sky {

	protected Vector color;
	
	public BasicSky(Vector color) {
		this.color = color;
	}
	
	public Vector getEmission(Vector direction) {
		return color;
	}

}
