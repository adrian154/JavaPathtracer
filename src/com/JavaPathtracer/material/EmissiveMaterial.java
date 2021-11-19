package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;
import com.JavaPathtracer.scene.Scene;

public class EmissiveMaterial extends BaseMaterial {

	public EmissiveMaterial(Sampleable emission) {
		super(emission);
	}
	
	@Override
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double ior) {
		return this.getColor(hit.textureCoord);
	}
	
	@Override
	public boolean shouldImportanceSample() {
		return true; // direct light samples towards emissive materials
	}
	
	@Override
	public String toString() {
		return String.format("Emissive %s", color.toString());
	}
	
}
