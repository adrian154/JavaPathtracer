package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class Checkerboard implements Sampleable {

	private static final Vector BLACK = new Vector(0.0, 0.0, 0.0);
	private static final Vector WHITE = new Vector(1.0, 1.0, 1.0);
	
	public Checkerboard() {
	}
	
	@Override
	public Vector sample(double u, double v) {
		return ((int)(u / 0.5) % 2 == 0) ^ ((int)(v / 0.5) % 2 == 0) ? BLACK : WHITE;
	}

}
