package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.tonemapping.LinearTonemapper;

public class Main {

	public static Camera createCamera() {
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.moveTo(new Vector(0.0, 180.0, 150.0));
		camera.setAngles(-Math.PI / 2, Math.PI / 2 + 0.2);
		//camera.setScale(50);
		camera.setFOV(10);
		return camera;
	}

	public static Raytracer createRaytracer() {
		return new Pathtracer(3);
		//return new DebugTracer1();
	}
	
	private static void startPreview(Texture output, Renderer renderer) {
		LivePreview preview = new LivePreview(output, renderer, 1);
		preview.start();
	}
	
	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = new TestScene();
		Raytracer raytracer = createRaytracer();
		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 3, new LinearTonemapper());
		startPreview(output, renderer);
		renderer.render(output);
		output.saveToFile(new File("output.png"));

	}

}