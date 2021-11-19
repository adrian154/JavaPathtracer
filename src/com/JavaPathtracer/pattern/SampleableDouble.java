package com.JavaPathtracer.pattern;

import com.JavaPathtracer.geometry.Vector;

public class SampleableDouble implements SampleableScalar {

	private double value;

	public SampleableDouble(double value) {
		this.value = value;
	}

	@Override
	public double sampleScalar(Vector textureCoords) {
		return value;
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}
	
}
