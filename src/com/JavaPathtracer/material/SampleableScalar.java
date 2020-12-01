package com.JavaPathtracer.material;

public class SampleableScalar implements ISampleableScalar {

	private double value;

	public SampleableScalar(double value) {
		this.value = value;
	}

	@Override
	public double sampleScalar(double u, double v) {
		return value;
	}

}
