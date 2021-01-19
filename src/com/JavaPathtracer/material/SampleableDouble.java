package com.JavaPathtracer.material;

public class SampleableDouble implements SampleableScalar {

	private double value;

	public SampleableDouble(double value) {
		this.value = value;
	}

	@Override
	public double sampleScalar(double u, double v) {
		return value;
	}

}
