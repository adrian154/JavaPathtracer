package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public interface Material {

	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer);

}
