package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.CombineMaterial;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.RoughMaterial;
import com.JavaPathtracer.material.SampleableScalar;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreviewRenderer;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.tonemapping.InverseTonemapper;

public class Main {

	public static Camera createCamera() {
	
		Camera camera = new Camera(new Vector(0.0, 0.0, 0.0));
		camera.setFOV(40);
		
		return camera;
		
	}
	
	public static Scene createScene() throws IOException {
		
		Scene scene = new Scene();
		
		// set up sky
		scene.setSkyEmission(new Vector(3.0));
		
		// materials
		IMaterial mat = new CombineMaterial(
			new DiffuseMaterial(new Vector(0x72/255.0), new Vector(0.0)),
			new RoughMaterial(new Vector(0x92/255.0, 0xf5/255.0, 0xf4/255.0), new Vector(0.0), new SampleableScalar(0.2)),
			new Texture(new File("assets/map3.png"))
		);
		
		scene.add(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.0, 0.0)), mat);
		scene.add(new Sphere(new Vector(0.0, 0.0, 3.0), 1.0), mat);
		
		return scene;
		
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = createScene();
		
		long start = System.currentTimeMillis();
		
		//Raytracer rt = new DebugTracer(camera, scene);
		Raytracer rt = new Pathtracer(5, 500, camera, scene, new InverseTonemapper());
		
		Renderer renderer = new LivePreviewRenderer(rt, 4, 2);
		renderer.render(output);
		
		long end = System.currentTimeMillis();
		System.out.println("Took " + (end - start) + "ms");

		output.saveToFile(new File("output.png"));
		System.out.println("Done.");
		
	}
	
	// for debugging
	public static String repeat(String str, int times) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < times; i++) {
			result.append(str);
		}
		return result.toString();
	}
	
}