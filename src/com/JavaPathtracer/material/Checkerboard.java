package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class Checkerboard implements Sampleable, SampleableScalar {

	private Vector color1, color2;

	public Checkerboard(Vector color1, Vector color2) {
		this.color1 = color1;
		this.color2 = color2;
	}

	public Checkerboard() {
		this(new Vector(1.0), new Vector(0.0));
	}

	@Override
	public Vector sample(double u, double v) {
		return ((int) (u / 0.5) % 2 == 0) ^ ((int) (v / 0.5) % 2 == 0) ? color1 : color2;
	}

	@Override
	public double sampleScalar(double u, double v) {
		return this.sample(u, v).x;
	}

}
