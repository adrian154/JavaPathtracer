package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Scene;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

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
	public Vector shade(Vector incident, Hit hit, int bounces, Scene scene, Pathtracer pathtracer) {
		return this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
	}
	
}
