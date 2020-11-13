package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public interface IMaterial {

	public Vector shade(Vector incident, Hit hit, int bounces, Pathtracer pathtracer);
	
}
