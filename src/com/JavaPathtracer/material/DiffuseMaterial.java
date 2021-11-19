package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;

public class DiffuseMaterial extends BRDFMaterial {

	public DiffuseMaterial(Sampleable color) {
		super(color, true);
	}
	
	@Override
	public double BRDF(Hit hit, Vector outgoing) {
		return 1 / Math.PI;
	}
	
	// always do light sampling
	@Override
	public boolean shouldSampleLights() {
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("Diffuse %s", color.toString());
	}

}
