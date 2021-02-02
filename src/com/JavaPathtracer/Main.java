package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.OrthoCamera;
import com.JavaPathtracer.debug.DebugTracer;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.tonemapping.LinearTonemapper;

public class Main {

	public static Camera createCamera() {
		
		OrthoCamera camera = new OrthoCamera();
		camera.enableJitter();
		camera.setScale(50);
		
		//camera.moveTo(Vector.fromSpherical(3.83, Math.PI / 2).times(150).plus(new Vector(0, 80, 0)));
		//camera.setLook(new Vector(0, 80, 0).minus(camera.getPos()).normalize());
		camera.moveTo(new Vector(0, 150, 0));
		
		return camera;
	
	}

	public static Raytracer createRaytracer() {
		return new DebugTracer(DebugTracer.Mode.ALBEDO);
	}
	
	private static void startPreview(Texture output, Renderer renderer) {
		LivePreview preview = new LivePreview(output, renderer, 2);
		preview.start();
	}

	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		
		Texture output = new Texture(outputImage);
		Camera camera = createCamera();
		Scene scene = new TestScene();
		Raytracer raytracer = createRaytracer();		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 16, new LinearTonemapper());

		startPreview(output, renderer);
		renderer.render(output);
		output.saveToFile(new File("output.png"));
		
	}

}