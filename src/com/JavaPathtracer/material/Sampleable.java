package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

// Things that can be sampled based on texture coordinate
public interface Sampleable {

	public Vector sample(double u, double v);
	
}
