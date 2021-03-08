package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

/*
 * NOTE: Use a Light instead of an emissive material for simple lights!
 * This should only really be used if you are blending emissive materials with non-emissive 
 * (...or something really funky)
 */
public class EmissiveMaterial extends BaseMaterial {

	public EmissiveMaterial(Sampleable emission) {
		super(emission);
	}
	
	@Override
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double ior) {
		return this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
	}
	
	@Override
	public String toString() {
		return String.format("Emissive %s", color.toString());
	}
	
}
