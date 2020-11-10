package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;
import com.JavaPathtracer.geometry.Circle;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreviewRenderer;
import com.JavaPathtracer.renderers.Renderer;

public class Main {

	public static Camera createCamera() {
	
		Camera camera = new Camera(new Vector(0.0, 4.0, -9.0));
		camera.setFOV(30);
		
		return camera;
		
	}
	
	public static Scene createScene() throws IOException {
		
		Scene scene = new Scene();
		
		// set up sky
		scene.setSkyEmission(new Vector(1.0, 1.0, 1.0).times(0.2));
		
		// materials
		Material orange = new DiffuseMaterial(new Vector(1.0, (double)0x73/0xff, 0.0), new Vector(0.0, 0.0, 0.0));	
		Material akarsh = new DiffuseMaterial(new Texture(new File("assets/AkarshSleeping.png")), new Vector(0.0, 0.0, 0.0));
		Material light = new DiffuseMaterial(new Vector(0.0, 0.0, 0.0), new Vector(1.0, 0.95, 0.9).times(30));
		
		// objects
		scene.add(new BVHMesh(new File("assets/OpenRoofBox.obj")), orange);
		scene.add(new Sphere(new Vector(3.0, 3.0, 6.0), 3.0), akarsh);
		scene.add(new Circle(new Vector(0.0, -1.0, 0.0), new Vector(1.5, 6.5, 3.5), 2.0), light);
		
		return scene;
		
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = createScene();
		
		long start = System.currentTimeMillis();
		
		//Raytracer rt = new DebugTracer(camera, scene);
		Raytracer rt = new Pathtracer(5, 100, camera, scene);
		
		Renderer renderer = new LivePreviewRenderer(rt, 4, 1);
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