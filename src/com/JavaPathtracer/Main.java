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
import com.JavaPathtracer.testscenes.TestScene2;
import com.JavaPathtracer.tonemapping.FilmicTonemapper;

public class Main {

	public static Camera createCamera() {
		
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.enableJitter();
		camera.setFOV(30);
		
		camera.moveTo(new Vector(-100, 800, 2000));
		camera.setAngles(-Math.PI / 2, 100 * Math.PI / 180);
		
		return camera;
	
	}
	
	public static Raytracer createRaytracer() {
		return new Pathtracer(5);
		//return new DebugTracer(DebugTracer.Mode.NORMAL);
	}
	
	private static void startPreview(RenderJob job) {
		LivePreview preview = new LivePreview(job, 2);
		preview.start();
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		// read args
		boolean preview = args.length > 0 && args[0].equals("nopreview");
				
		// set up objs
		BufferedImage outputImage = new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		Camera camera = createCamera();
		Scene scene = new TestScene2();
		Raytracer raytracer = createRaytracer();		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 3, new FilmicTonemapper());

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