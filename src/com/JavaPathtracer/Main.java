package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreview;
import com.JavaPathtracer.renderers.Renderer;
import com.JavaPathtracer.tonemapping.FilmicTonemapper;

public class Main {

	public static Camera createCamera() {
		OrthographicCamera camera = new OrthographicCamera();
		camera.moveTo(new Vector(0.0, 150.0, 50.0));
		camera.setAngles(-Math.PI / 2, Math.PI / 2);
		camera.setScale(50);
		return camera;
	}

	public static Scene createScene() throws IOException {

		
		Scene scene = new Scene();
		
		// set up sky
		scene.setSkyEmission(new Vector(20.0));

		scene.add(new BVHMesh(new File("assets/girl/Camellia.obj")), new DiffuseMaterial(new Texture("assets/girl/Textures/Camellia_Body_diffuse.jpg")));
		//scene.add(new BVHMesh(new File("assets/girl/Camellia.obj")), new DiffuseMaterial());
		
		return scene;
		
	}

	public static Raytracer createRaytracer() {
		//return new Pathtracer(3);
		return new DebugTracer();
	}
	
	private static void startPreview(Texture output, Renderer renderer) {
		LivePreview preview = new LivePreview(output, renderer, 1);
		preview.start();
	}
	
	public static void main(String[] args) throws IOException {

		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = createCamera();
		Scene scene = createScene();
		Raytracer raytracer = createRaytracer();
		
		Renderer renderer = new Renderer(scene, camera, raytracer, 16, 256, new FilmicTonemapper());
		startPreview(output, renderer);
		renderer.render(output);
		output.saveToFile(new File("output.png"));

	}

}