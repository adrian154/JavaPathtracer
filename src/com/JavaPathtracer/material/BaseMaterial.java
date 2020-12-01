package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public abstract class BaseMaterial implements IMaterial {

	private ISampleable color;

	public BaseMaterial(ISampleable color) {
		this.color = color;
	}

	public Vector getColor(double u, double v) {
		return color.sample(u, v);
	}

}
