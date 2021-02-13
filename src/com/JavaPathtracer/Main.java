package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderer.LivePreview;
import com.JavaPathtracer.renderer.RenderJob;
import com.JavaPathtracer.renderer.Renderer;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.testscenes.TestScene3;
import com.JavaPathtracer.tonemapping.ACESTonemapper;

public class Main {

	public static Camera createCamera() {
		
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.enableJitter();
		camera.setFOV(30);
		
		camera.moveTo(new Vector(0, 0, 3));
		camera.setAngles(-Math.PI / 2, Math.PI / 2);
		
		return camera;
	
	}
	
	public static Raytracer createRaytracer() {
		return new Pathtracer(5);
		//return new DebugTracer(DebugTracer.Mode.ALBEDO);
	}
	
	private static void startPreview(RenderJob job) {
		LivePreview preview = new LivePreview(job, 1);
		preview.start();
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		// read args
		boolean preview = !(args.length > 0 && args[0].equals("nopreview"));
				
		// set up objs
		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		Camera camera = createCamera();
		Scene scene = new TestScene3();
		Raytracer raytracer = createRaytracer();		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 512, new ACESTonemapper());

		// render
		Stopwatch stopwatch = new Stopwatch("Render");
		RenderJob job = renderer.render(output);
		if(preview) {
			startPreview(job);
		}
		job.await();
		stopwatch.stop();
		
		// save
		output.saveToFile(new File("output.png"));
		
	}

}