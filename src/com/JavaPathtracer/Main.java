package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;
import com.JavaPathtracer.geometry.Matrix;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Square;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.ParallelRenderer;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.tonemapping.InverseTonemapper;

public class Main {

	public static Camera createCamera() {

		Camera camera = new Camera(new Vector(0, 70, -120));
		camera.setFOV(60);
		//camera.look
		
		
		return camera;

	}

	public static Scene createScene() throws IOException {

		Scene scene = new Scene();

		// set up sky
		scene.setSkyEmission(new Vector(0x87/255.0, 0xce/255.0, 0xfa/255.0).times(10));

		Matrix matrix = new Matrix();
		IMaterial matdiff = new DiffuseMaterial(new Vector(1.0, 1.0, 1.0));
		Shape mesh = new BVHMesh(new File("assets/toilet_ambience.obj"), matrix);
		scene.add(mesh, matdiff);

		IMaterial light = new EmissiveMaterial(new Vector(0xfe/255.0, 0xf8/255.0, 0xd0/255.0).times(70));
		scene.add(new Square(new Vector(0.0, -1.0, 0.0), new Vector(1250.0, 2230, 1250.0), 200), light);
		
		return scene;

	}

	private static void startPreview(Texture output) {
		LivePreview preview = new LivePreview(output, 1);
		preview.start();
	}

	private static void startRender(Camera camera, Scene scene, Texture output) {
		Pathtracer rt = new Pathtracer(5, 300, camera, scene, new InverseTonemapper());
		Renderer renderer = new ParallelRenderer(rt, 8, 8);
		renderer.render(output);
	}
	
	private static void startRenderDebug(Camera camera, Scene scene, Texture output)  {
		DebugTracer rt = new DebugTracer(camera, scene);
		Renderer renderer = new ParallelRenderer(rt, 8, 8);
		renderer.render(output);
	}

	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(1280/2, 720/2, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);

		Camera camera = createCamera();
		Scene scene = createScene();

		long start = System.currentTimeMillis();
		startPreview(output);
		startRenderDebug(camera, scene, output);
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