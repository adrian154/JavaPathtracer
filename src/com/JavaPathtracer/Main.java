package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;
import com.JavaPathtracer.geometry.Matrix;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Square;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.tonemapping.FilmicTonemapper;

public class Main {

	public static Camera createCamera() {

		Camera camera = new Camera(new Vector(28, 25, 35), new Vector(0, 0, -1), new Vector(0, 1, 0));
		camera.setFOV(80);
		return camera;

	}

	public static Scene createScene() throws IOException {

		Scene scene = new Scene();

		// set up sky
		scene.setSkyEmission(new Vector(0x87/255.0, 0xce/255.0, 0xfa/255.0).times(1));

		Matrix matrix = new Matrix(new double[] {
			1, 0, 0, 0,
			0, 0, 1, 0,
			0, 1, 0, 0,
			0, 0, 0, 0
		});
		IMaterial matdiff = new DiffuseMaterial(new Vector(1.0, 1.0, 1.0));
		Shape mesh = new BVHMesh(new File("assets/tinker.obj"), matrix);
		scene.add(mesh, matdiff);

		IMaterial light = new EmissiveMaterial(new Vector(0xfe/255.0, 0xf8/255.0, 0xd0/255.0).times(70));
		Square square = new Square(new Vector(0.0, -1.0, 0.0), new Vector(28, 45, 18), 10);
		scene.add(square, light);
		scene.add(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, 0.0, 0.0)), new DiffuseMaterial(new Vector(1.0, 1.0, 1.0)));
		
		return scene;

	}

	public static Raytracer createRaytracer() {
		return new Pathtracer(3);
	}
	
	private static void startPreview(Texture output, Renderer renderer) {
		LivePreview preview = new LivePreview(output, renderer, 3);
		preview.start();
	}
	
	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = createScene();
		Raytracer raytracer = createRaytracer();
		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 100, new FilmicTonemapper());
		startPreview(output, renderer);
		renderer.render(output);
		output.saveToFile(new File("output.png"));

	}

	// for debugging
	public static String repeat(String str, int times) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < times; i++) {
			result.append(str);
		}
		return result.toString();
	}

}