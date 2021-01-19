package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Scene;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public interface Material {

	public Vector shade(Vector incident, Hit hit, int bounces, Scene scene, Pathtracer pathtracer);

}
