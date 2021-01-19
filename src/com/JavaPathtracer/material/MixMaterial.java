package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Scene;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

// This class is kind of terrible and reflects the flawed nature of the abstractions I have chosen for materials
// The ideal way to implement this would be to pick a material randomly and use it for the rest of the pathtrace() cycle
// However, that's not possible in the current IMaterial system. Trying to do so would be jamming a square peg into a round hole.
// So... oh well.
public class MixMaterial implements Material {

	public Material A, B;
	public SampleableScalar proportion;

	public MixMaterial(Material A, Material B, SampleableScalar proportion) {
		this.A = A;
		this.B = B;
		this.proportion = proportion;
	}

	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Scene scene,Pathtracer pathtracer) {
		return (Math.random() < proportion.sampleScalar(hit.textureCoordinates.x, hit.textureCoordinates.y) ? A : B)
				.shade(incident, hit, bounces, scene, pathtracer);
	}

}
