package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;
import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Matrix;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Square;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.CombineMaterial;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.MirrorMaterial;
import com.JavaPathtracer.material.SampleableScalar;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.ParallelRenderer;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.tonemapping.FilmicTonemapper;

public class Main {

	public static Camera createCamera() {
	
		Camera camera = new Camera(new Vector(1.0, 0.5, -1.5));
		camera.lookAt(new Vector(0.0, 0.0, 2.0));
		camera.setFOV(56);
		
		return camera;
		
	}
	
	public static Scene createScene() throws IOException {
		
		Scene scene = new Scene();
		
		// set up sky
		scene.setSkyEmission(new Vector(2.0));
	
		// Walls
		int width = 7;
		int height = 5;
		int depth = 6;
		IMaterial white = new DiffuseMaterial(new Vector(1.0, 1.0, 1.0), new Vector(0.0));
		IMaterial oak = new DiffuseMaterial(new Texture(new File("assets/planks_oak.png")), new Vector(0.0));
		IMaterial lwall = new DiffuseMaterial(new Vector(0xe6/255.0, 0x91/255.0, 0x50/255.0), new Vector(0.0));
		IMaterial rwall = new DiffuseMaterial(new Vector(0x50/255.0, 0x9d/255.0, 0xe6/255.0), new Vector(0.0));
		IMaterial floor = new DiffuseMaterial(new Texture(new File("assets/stonebrick.png")), new Vector(0.0));
		scene.add(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -height/2, 0.0)), floor);
		scene.add(new Plane(new Vector(0.0, -1.0, 0.0), new Vector(0.0, height/2, 0.0)), white);
		scene.add(new Plane(new Vector(1.0, 0.0, 0.0), new Vector(-width/2, 0.0, 0.0)), lwall);
		scene.add(new Plane(new Vector(-1.0, 0.0, 0.0), new Vector(width/2, 0.0, 0.0)), rwall);
		scene.add(new Plane(new Vector(0.0, 0.0, 1.0), new Vector(0.0, 0.0, -depth/2)), white);
		scene.add(new Plane(new Vector(0.0, 0.0, -1.0), new Vector(0.0, 0.0, depth/2)), oak);

		// add light
		IMaterial light = new MirrorMaterial(new Vector(0.0, 0.0, 0.0), new Vector(1.0, 1.0, 0xd5/255.0).times(100.0));
		scene.add(new Square(new Vector(0.0, -1.0, 0.0), new Vector(0.0, height/2-0.05, 1.5), 0.75), light);
		
		// add spot
		Matrix matrix = Matrix.Translate(1.5, -height/2 + 0.736784, 1.5).multiply(Matrix.RotateY(0.5));
		IMaterial matdiff = new DiffuseMaterial(new Texture(new File("assets/spot/spot.png")), new Vector(0.0));
		scene.add(new BVHMesh(new File("assets/spot/spot.obj"), matrix), matdiff);
		
		// add teapot
		IMaterial ceramic = new CombineMaterial(
			new DiffuseMaterial(new Vector(1.0), new Vector(0.0)),
			new MirrorMaterial(new Vector(1.0), new Vector(0.0)),
			new SampleableScalar(0.9)
		);
		Matrix matrix2 = Matrix.Translate(0.0, -height/2, 0.8).multiply(Matrix.Scale(0.3));
		scene.add(new BVHMesh(new File("assets/UtahTeapot.obj"), matrix2), ceramic);
		
		// add pedestal + globe
		scene.add(new BoundingBox(new Vector(-2.0, -height/2, 1.0), new Vector(-1.0, -height/2 + 1.0, 2.0)), white);		
		IMaterial earth = new DiffuseMaterial(new Texture(new File("assets/earth.jpg")), new Vector(0.0));
		scene.add(new Sphere(new Vector(-1.5, -height/2 + 1.0 + 0.5, 1.5), 0.5), earth);
		
		return scene;
		
	}
	
	public static void main(String[] args) throws IOException {
		
		BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = createScene();
		
		long start = System.currentTimeMillis();
		
		//Raytracer rt = new DebugTracer(camera, scene);
		Raytracer rt = new Pathtracer(7, 10, camera, scene, new FilmicTonemapper());
		LivePreview preview = new LivePreview(output, 2);
		Renderer renderer = new ParallelRenderer(rt, 4);
		preview.start();
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