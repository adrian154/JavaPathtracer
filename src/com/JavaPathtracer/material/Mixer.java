package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.SampleableScalar;
import com.JavaPathtracer.scene.Scene;

// This class is kind of terrible and reflects the flawed nature of the abstractions I have chosen for materials
// The ideal way to implement this would be to pick a material randomly and use it for the rest of the pathtrace() cycle
// However, that's not possible in the current IMaterial system. Trying to do so would be jamming a square peg into a round hole.
// So... oh well.
public class Mixer implements Material {

	public Material A, B;
	public SampleableScalar proportion;

	public Mixer(Material A, Material B, SampleableScalar proportion) {
		this.A = A;
		this.B = B;
		this.proportion = proportion;
	}

	@Override
	public Vector shade(Hit hit, int bounces, Scene scene,Pathtracer pathtracer, double ior) {
		double val = proportion.sampleScalar(hit.textureCoord);
		return A.shade(hit, bounces, scene, pathtracer, ior).times(val).plus(B.shade(hit, bounces, scene, pathtracer, ior).times(1 - val));
	}
	
	@Override
	public String toString() {
		return String.format("Mix (%s/%s) using %s", A.toString(), B.toString(), proportion.toString());
	}
	
	@Override
	public boolean shouldImportanceSample() {
		return A.shouldImportanceSample() || B.shouldImportanceSample();
	}
	
	@Override
	public Vector getDebugColor(Vector textureCoord) {
		return A.getDebugColor(textureCoord).plus(B.getDebugColor(textureCoord)).divBy(2);
	}

}
