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
import com.JavaPathtracer.tonemapping.FilmicTonemapper;

public class Main {

	public static Camera createCamera() {
		PerspectiveCamera camera = new PerspectiveCamera();
		camera.setFOV(5);
		camera.enableJitter();
		return camera;
	}

	public static Raytracer createRaytracer() {
		return new Pathtracer(5);
		//return new DebugTracer2();
	}
	
	private static void startPreview(Texture output, Renderer renderer) {
		LivePreview preview = new LivePreview(output, renderer, 1);
		preview.start();
	}
	
	private static void renderFrame(Camera camera, Scene scene, Raytracer raytracer, Renderer renderer, Texture output, int which) throws IOException {
		double angle = which / 180.0 * Math.PI;
		Vector newPos = Vector.fromSpherical(angle, Math.PI / 2).times(500).plus(new Vector(0, 200, 0));
		camera.moveTo(newPos);
		camera.setLook(new Vector(0, 100, 0).minus(camera.getPos()).normalize());
		renderer.render(output);
		output.saveToFile(new File("anim/frame" + which + ".png"));
	}
	
	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		
		Texture output = new Texture(outputImage);
		Camera camera = createCamera();
		Scene scene = new TestScene();
		Raytracer raytracer = createRaytracer();		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 256, new FilmicTonemapper());

		startPreview(output, renderer);
		for(int i = 240; i < 241; i++) {
			System.out.println("Rendering frame " + i);
			renderFrame(camera, scene, raytracer, renderer, output, i);
		}
		
	}

}