package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.Mesh;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderers.LivePreviewRenderer;
import com.JavaPathtracer.renderers.Renderer;

public class Main {

	public static void main(String[] args) throws IOException {
		
		BufferedImage outputImage = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		Texture output = new Texture(outputImage);
		
		Camera camera = new Camera(new Vector(1.0, 3.0, -3.0));
		Scene scene = new Scene();
		
		//scene.setSkyEmission(new HDRMap(new File("assets/sky_cloudy/HDR_029_Sky_Cloudy_Ref.hdr")));
		scene.setSkyEmission(new Vector(1, 1, 1));
		
		Material mat = new Material(new Vector(1.0, 1.0, 1.0), new Vector(1.0, 1.0, 1.0).times(0.0));
		Material earthmat = new Material(new Texture(new File("assets/earth.jpg")), new Vector(1.0, 1.0, 1.0).times(0.0));
		scene.add(new WorldObject(new Sphere(new Vector(-1.5, 1.0, 7.0), 2.0), earthmat));
		scene.add(new WorldObject(new Sphere(new Vector(1.0, 0.5, 4.0), 0.5), mat));
		scene.add(new WorldObject(new Plane(new Vector(0.0, 1.0, 0.0), new Vector(0.0, -1.0, 0.0)), mat));
		scene.add(new WorldObject(new Mesh(new File("assets/UtahTeapot.obj")), mat));
		
		//Raytracer rt = new Pathtracer(5, 100, camera, scene);
		Raytracer rt = new DebugTracer(camera, scene);
		Renderer renderer = new LivePreviewRenderer(rt, 4, 2);
		renderer.render(output);

		output.saveToFile(new File("output.png"));
		System.out.println("Done.");
		
	}
	
}