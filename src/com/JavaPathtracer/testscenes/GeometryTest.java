package com.JavaPathtracer.testscenes;

import java.io.IOException;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.pattern.Checkerboard;
import com.JavaPathtracer.scene.Scene;

public class GeometryTest extends Scene {
	
	public GeometryTest() throws IOException {
		
		this.add(new Plane(new Vector(0, 1, 0), new Vector(0, 0, 0), 2), new DiffuseMaterial(new Checkerboard()));
		
		Material mtl = new DiffuseMaterial(Vector.ONE);
		this.add(new BoundingBox(new Vector(1, 1, 1), new Vector(2, 2, 2)), mtl);
		this.add(new Sphere(new Vector(3, 2, 0), 1.0), mtl);
				
	}
	
}
