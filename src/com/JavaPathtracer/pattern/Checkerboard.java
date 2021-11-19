package com.JavaPathtracer.pattern;

import com.JavaPathtracer.geometry.Vector;

public class Checkerboard implements Sampleable {

	private Vector color1, color2;

	public Checkerboard(Vector color1, Vector color2) {
		this.color1 = color1;
		this.color2 = color2;
	}

	public Checkerboard() {
		this(new Vector(1.0), new Vector(0.0));
	}

	@Override
	public Vector sample(Vector textureCoord) {
		return (textureCoord.x > 0.5 ^ textureCoord.y > 0.5) ? color1 : color2;
	}
	
	@Override
	public String toString() {
		return String.format("Checkerboard %s/%s", color1.toString(), color2.toString());
	}
	
}
