package com.JavaPathtracer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.JavaPathtracer.geometry.Matrix;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.mesh.MeshObject;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.SimpleSky;

public class TestScene extends Scene {

	public TestScene() throws IOException {
		
		super();
		this.setSky(new SimpleSky(new Vector(1)));
		//this.setSky(new DirectionalSky(new Vector(1, 0, 1), new Vector(80), Vector.ZERO));
		//this.add(new Square(new Vector(0.0, -1.0, 0.0), new Vector(0.0, 200.0, 0.0), 20.0), new EmissiveMaterial(new Vector(300.0)));
		//this.add(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0), 100), new DiffuseMaterial(Vector.ONE));
		
		this.add(new MeshObject(
			new File("assets/girl/Camellia.obj"),
			new Matrix(),
			Map.of(
				"Material11896", new DiffuseMaterial(new Texture(new File("assets/girl/textures/Camellia_Armleg_diffuse.jpg"))),
				"Material11891", new DiffuseMaterial(new Texture(new File("assets/girl/textures/Camellia_Leye_diffuse.jpg"))),
				"Material11899", new DiffuseMaterial(new Texture(new File("assets/girl/textures/Camellia_Body_diffuse.jpg"))),
				"Material11893", new DiffuseMaterial(new Texture(new File("assets/girl/textures/Camellia_Reye_diffuse.png"))),
				"Material11902", new DiffuseMaterial(new Texture(new File("assets/girl/textures/Camellia_Face_diffuse.jpg"))),
				"Material11886", new DiffuseMaterial(new Texture(new File("assets/girl/textures/Camellia_Braid_diffuse.png")))	
			)
		));
		
	}
	
}
