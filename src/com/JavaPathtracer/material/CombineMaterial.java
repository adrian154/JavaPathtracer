package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

public class CombineMaterial implements IMaterial {
	
	private IMaterial A, B;
	private ISampleableScalar proportion;
	
	public CombineMaterial(IMaterial A, IMaterial B, ISampleableScalar proportion) {
		this.A = A;
		this.B = B;
		this.proportion = proportion;
	}

	public Vector scatter(double u, double v, Vector incident, Vector normal) {
		return (Math.random() < proportion.sampleScalar(u, v) ? A : B).scatter(u,  v, incident, normal);
	}
	
	public Vector getEmission(double u, double v) {
		double prop = proportion.sampleScalar(u, v);
		return A.getEmission(u, v).times(prop).plus(B.getEmission(u, v).times(1 - prop));
	}
	
	public Vector getColor(double u, double v) {
		double prop = proportion.sampleScalar(u, v);
		return A.getColor(u, v).times(prop).plus(B.getColor(u, v).times(1 - prop));
	}
	
	// TODO: Think about whether this is such a good idea.
	public boolean doDotProduct(double u, double v) {
		return proportion.sampleScalar(u, v) > 0.5 ? A.doDotProduct(u, v) : B.doDotProduct(u, v);
	}
	
}
