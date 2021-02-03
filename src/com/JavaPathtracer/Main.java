package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.OrthoCamera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderer.LivePreview;
import com.JavaPathtracer.renderer.RenderJob;
import com.JavaPathtracer.renderer.Renderer;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.testscenes.TestScene;
import com.JavaPathtracer.tonemapping.LinearTonemapper;

public class Main {

	public static Camera createCamera() {
		
		OrthoCamera camera = new OrthoCamera();
		camera.enableJitter();
		camera.setScale(30);
		
		camera.moveTo(Vector.fromSpherical(3.83, Math.PI / 2).times(150).plus(new Vector(0, 80, 0)));
		camera.setLook(new Vector(0, 80, 0).minus(camera.getPos()).normalize());

		return camera;
	
	}
	
	public static Raytracer createRaytracer() {
		//return new Pathtracer(2);
		return new DebugTracer(DebugTracer.Mode.TEST);
	}
	
	private static void startPreview(RenderJob job) {
		LivePreview preview = new LivePreview(job, 2);
		preview.start();
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		// set up objs
		BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		Camera camera = createCamera();
		Scene scene = new TestScene();
		Raytracer raytracer = createRaytracer();		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 256, new LinearTonemapper());

		// render
		Stopwatch stopwatch = new Stopwatch("Render");
		RenderJob job = renderer.render(output);
		startPreview(job);
		job.await();
		stopwatch.stop();
		
		// save
		output.saveToFile(new File("output.png"));
		
	}

}