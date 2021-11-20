package com.JavaPathtracer.testscenes;

import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.scene.Scene;

public class GeometryTest extends Scene {
	
	public GeometryTest() {
		
		this.add(new Sphere(new Vector(0, 0, 5), 1.0), new DiffuseMaterial(new Vector(0x00ffff)));
		
	}
	
}
