package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.bvh.BVHMesh;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreviewRenderer;
import com.JavaPathtracer.renderers.Renderer;

public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedImage outputImage = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = new Camera(new Vector(4.0, 4.0, -7.0));
		camera.setFOV(30);
		camera.lookAt(new Vector(0.0, 0.0, 0.0));
		Scene scene = new Scene();
		
		//scene.setSkyEmission(new HDRMap(new File("assets/sky_cloudy/HDR_029_Sky_Cloudy_Ref.hdr")));
		scene.setSkyEmission(new Vector(1, 1, 1));
		
		Material mat = new Material(new Vector(1.0, 1.0, 1.0), new Vector(1.0, 1.0, 1.0).times(0.0));
		scene.add(new WorldObject(new BVHMesh(new File("assets/UtahTeapot.obj")), mat));

		long start = System.currentTimeMillis();
		
		Raytracer rt = new DebugTracer(camera, scene);
		//Raytracer rt = new Pathtracer(5, 100, camera, scene);
		
		Renderer renderer = new LivePreviewRenderer(rt, 4, 1);
		renderer.render(output);
		
		long end = System.currentTimeMillis();
		System.out.println("Took " + (end - start));

		output.saveToFile(new File("output.png"));
		System.out.println("Done.");
		
	}
	
	// for debugging
	public static String repeat(String str, int times) {
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < times; i++) {
			result.append(str);
		}
		return result.toString();
	}
	
}