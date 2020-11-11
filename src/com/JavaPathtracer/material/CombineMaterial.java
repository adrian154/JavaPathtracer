package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;

// This class is kind of terrible and reflects the flawed nature of the abstractions I have chosen for materials
// The ideal way to implement this would be to pick a material randomly and use it for the rest of the pathtrace() cycle
// However, that's not possible in the current IMaterial system. Trying to do so would be jamming a square peg into a round hole.
// So... oh well.
public class CombineMaterial implements IMaterial {
	
	public IMaterial A, B;
	public ISampleableScalar proportion;
	
	public CombineMaterial(IMaterial A, IMaterial B, ISampleableScalar proportion) {
		this.A = A;
		this.B = B;
		this.proportion = proportion;
	}

	@Deprecated
	public Vector scatter(double u, double v, Vector incident, Vector normal) {
		return (Math.random() < proportion.sampleScalar(u, v) ? A : B).scatter(u,  v, incident, normal);
	}
	
	@Deprecated
	public Vector getEmission(double u, double v) {
		double prop = proportion.sampleScalar(u, v);
		return A.getEmission(u, v).times(prop).plus(B.getEmission(u, v).times(1 - prop));
	}
	
	@Deprecated
	public Vector getColor(double u, double v) {
		double prop = proportion.sampleScalar(u, v);
		return A.getColor(u, v).times(prop).plus(B.getColor(u, v).times(1 - prop));
	}
	
	@Deprecated
	public boolean doDotProduct(double u, double v) {
		return Math.random() > proportion.sampleScalar(u, v) ? A.doDotProduct(u, v) : B.doDotProduct(u, v);
	}
	
}
