package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public interface IMaterial {

	public Vector getColor(double u, double v);
	public Vector getEmission(double u, double v);
	public Vector scatter(double u, double v, Vector incident, Vector normal);
	public boolean doDotProduct(double u, double v);
	
}
