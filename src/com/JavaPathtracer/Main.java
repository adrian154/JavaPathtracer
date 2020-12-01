package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;
import com.JavaPathtracer.geometry.Matrix;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.ParallelRenderer;
import com.JavaPathtracer.renderers.Renderer;

public class Main {

	public static Camera createCamera() {

		Camera camera = new Camera(new Vector(0.0, 0.0, 1.0));
		camera.lookAt(new Vector());
		camera.setFOV(60);

		return camera;

	}

	public static Scene createScene() throws IOException {

		Scene scene = new Scene();

		// set up sky
		scene.setSkyEmission(new Vector(2.0));
		IMaterial white = new DiffuseMaterial(new Vector(1.0));

		Matrix matrix = new Matrix();
		IMaterial matdiff = new DiffuseMaterial(new Texture(new File("assets/AfricanHead.png")));
		Shape mesh = new BVHMesh(new File("assets/AfricanHead.obj"), matrix);
		scene.add(mesh, matdiff);

		return scene;

	}

	private static void startPreview(Texture output) {
		LivePreview preview = new LivePreview(output, 2);
		preview.start();
	}

	private static void startRender(Camera camera, Scene scene, Texture output) {
		Raytracer rt = new DebugTracer(camera, scene);
		// Pathtracer rt = new Pathtracer(7, 100, camera, scene, new
		// InverseTonemapper());
		Renderer renderer = new ParallelRenderer(rt, 2);
		renderer.render(output);
	}

	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);

		Camera camera = createCamera();
		Scene scene = createScene();

		long start = System.currentTimeMillis();
		startPreview(output);
		startRender(camera, scene, output);
		long end = System.currentTimeMillis();

		System.out.println("Took " + (end - start) + "ms");
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