package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Checkerboard;
import com.JavaPathtracer.material.CombineMaterial;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.MirrorMaterial;
import com.JavaPathtracer.material.RoughMaterial;
import com.JavaPathtracer.material.SampleableScalar;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreviewRenderer;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.tonemapping.InverseTonemapper;

public class Main {

	public static Camera createCamera() {
	
		Camera camera = new Camera(new Vector(0.0, 0.0, -3.0));
		camera.lookAt(new Vector(0, 0, 0));
		camera.setFOV(30);
		
		return camera;
		
	}
	
	public static Scene createScene() throws IOException {
		
		Scene scene = new Scene();
		
		// set up sky
		scene.setSkyEmission(new Vector(2.0));
		
		/*
		// materials
		IMaterial matdiff = new DiffuseMaterial(new Texture(new File("assets/spot/spot.png")), new Vector(0.0));
		scene.add(new BVHMesh(new File("assets/spot/spot.obj")), matdiff);
		
		// Walls
		int width = 5;
		int height = 5;
		int depth = 3;
		IMaterial wall = new DiffuseMaterial(new Vector(1.0, 1.0, 1.0), new Vector(0.0));
		scene.add(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -height/2, 0.0)), wall);
		scene.add(new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, height/2, 0.0)), wall);
		scene.add(new Plane(new Vector(1.0, 0.0, 0.0), new Vector(-width/2, 0.0, 0.0)), wall);
		scene.add(new Plane(new Vector(-1.0, 0.0, 0.0), new Vector(width/2, 0.0, 0.0)), wall);
		scene.add(new Plane(new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -depth/2)), wall);
		scene.add(new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, depth/2)), wall);
		*/
		
		IMaterial stone = new DiffuseMaterial(new Vector(0x6e/255.0), new Vector(0.0));
		IMaterial gold = new CombineMaterial(stone, new RoughMaterial(new Vector(0xf1/255.0, 0xd2/255.0, 0x3e/255.0), new Vector(0.0), new SampleableScalar(0.4)), new Texture(new File("assets/map7.png")));
		IMaterial diamond = new CombineMaterial(stone, new RoughMaterial(new Vector(0x87/255.0, 0xec/255.0, 0xdc/255.0), new Vector(0.0), new SampleableScalar(0.2)), new Texture(new File("assets/map3.png")));
		IMaterial red = new CombineMaterial(new RoughMaterial(new Vector(1.0, 0.0, 0.0), new Vector(0.0), new SampleableScalar(0.1)), stone, new Checkerboard());
		IMaterial emissive = new MirrorMaterial(new Vector(0.0), new Vector(0x42/255.0, 0xf5/255.0, 0xb0/255.0).times(20.0));
		
		scene.add(new Sphere(new Vector(-1.0, 0.3, 3.0), 1.3), gold);
		scene.add(new Sphere(new Vector(0.5, -0.3, 1.8), 0.7), red);
		scene.add(new Sphere(new Vector(-0.75,-0.5, 1.6), 0.5), emissive);
		scene.add(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.0, 0.0)), diamond);
		
		return scene;
		
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = createScene();
		
		long start = System.currentTimeMillis();
		
		//Raytracer rt = new DebugTracer(camera, scene);
		Raytracer rt = new Pathtracer(5, 500, camera, scene, new InverseTonemapper());
		
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