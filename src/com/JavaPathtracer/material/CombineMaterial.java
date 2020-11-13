package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
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

	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Pathtracer pathtracer) {
		return (Math.random() < proportion.sampleScalar(hit.textureCoordinates.x, hit.textureCoordinates.y) ? A : B).shade(incident, hit, bounces, pathtracer);
	}
	
}
