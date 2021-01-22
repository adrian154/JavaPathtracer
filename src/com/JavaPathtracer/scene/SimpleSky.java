package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

public class SimpleSky implements Sky {

	protected Vector color;
	
	public SimpleSky(Vector color) {
		this.color = color;
	}
	
	public Vector getEmission(Vector direction) {
		return color;
	}

}
