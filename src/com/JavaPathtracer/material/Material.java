package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public interface Material {

	// TODO: move IOR to ObjectHit perhaps?
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double ior);
	public Vector getDebugColor();
	public boolean shouldImportanceSample();
	
}
