package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderer.InteractivePreview;
import com.JavaPathtracer.renderer.LivePreview;
import com.JavaPathtracer.renderer.RenderJob;
import com.JavaPathtracer.renderer.Renderer;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.testscenes.TestScene4;
import com.JavaPathtracer.tonemapping.ACESTonemapper;

public class Main {

	private static Camera camera;
	private static Raytracer raytracer;
	private static Scene scene;
	private static Renderer renderer;
	private static Texture output;
	
	private static void createCamera() {
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.enableJitter();
		camera.moveTo(new Vector(-2.123775645702241, 2.7755575615628914E-17, -5.176095142652289)); camera.setAngles(1.310796, 1.570796); camera.setFOV(30.000000);
		//camera.disableJitter();
		Main.camera = camera;
	}
	
	private static void createRaytracer() {
		raytracer = new Pathtracer(5);
		//raytracer = new DebugTracer(DebugTracer.Mode.SIMPLE_SHADED);
	}
	
	private static void render(boolean preview, String outputName) throws InterruptedException, IOException {
		Stopwatch sw = new Stopwatch("Render");
		RenderJob job = renderer.render(output);
		if(preview) {
			LivePreview previewObj = new LivePreview(job, 2);
			previewObj.start();
		}
		job.await();
		sw.stop();
		output.saveToFile(new File(outputName));
	}
	
	private static void interactive() throws InterruptedException {
		InteractivePreview preview = new InteractivePreview(renderer, camera, output, 4);
		preview.run();
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {

		// read args
		String mode = "render-preview";
		if(args.length > 0) mode = args[0];
				
		// set up output objects
		output = new Texture(new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB));
		
		// set up renderer objects
		createCamera();
		createRaytracer();
		scene = new TestScene4();
		renderer = new Renderer(scene, camera, raytracer, 16, 512, new ACESTonemapper());
		
		if(mode.equals("render")) {
			render(false, "output.png");
		} else if(mode.equals("render-preview")) {
			render(true, "output.png");
		} else if(mode.equals("interactive")) {
			interactive();
		}
		
	}

}