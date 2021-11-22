package com.JavaPathtracer.testscenes;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.mesh.OBJLoader;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.pattern.Texture;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.SimpleSky;

public class GeometryTest extends Scene {
	
	public GeometryTest() throws IOException {
		
		this.setSky(new SimpleSky(Vector.ONE.times(10)));
		
		//this.add(new Plane(new Vector(0, 1, 0), new Vector(0, 0, 0), 2), new DiffuseMaterial(new Checkerboard()));
		
		Material mtl = new DiffuseMaterial(Vector.ONE);
		this.add(new BoundingBox(new Vector(1, 1, 1), new Vector(2, 2, 2)), mtl);
		this.add(new Sphere(new Vector(3, 2, 0), 1.0), mtl);
		
		Material matdiff = new DiffuseMaterial(new Texture(new File("assets/spot/spot.png")));
        this.add(OBJLoader.load("assets/spot/spot.obj", Map.of("", matdiff), /*new Transform().translate(1.5, -height/2 + 0.736, 1.5).rotateY(0.5).complete()*/null));
		
	}
	
}
