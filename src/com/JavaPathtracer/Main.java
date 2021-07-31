package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.DebugTracer.Mode;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.renderer.InteractivePreview;
import com.JavaPathtracer.renderer.LivePreview;
import com.JavaPathtracer.renderer.RenderJob;
import com.JavaPathtracer.renderer.Renderer;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.testscenes.Scene9;
import com.JavaPathtracer.tonemapping.ACESTonemapper;

public class Main {
	
	private static Raytracer raytracer;
	private static Scene scene;
	private static Renderer renderer;
	private static Texture output;
	
	private static void createRaytracer() {
		raytracer = new Pathtracer(5);
		//raytracer = new DebugTracer(Mode.SIMPLE_SHADED);
	}
	
	private static void render(boolean preview, String outputName) throws InterruptedException, IOException {
		Stopwatch sw = new Stopwatch("Render");
		RenderJob job = renderer.render(output);
		if(preview) {
			LivePreview previewObj = new LivePreview(job, 1);
			previewObj.start();
		}
		job.await();
		sw.stop();
		output.saveToFile(new File(outputName));
	}
	
	private static void animate() throws InterruptedException, IOException {
		for(int i = 0; i < 360; i++) {
			scene.update(i);
			RenderJob job = renderer.render(output);
			job.await();
			output.saveToFile(new File("animation/frame" + i + ".png"));
			System.out.println("Finished frame " + i);
		}
	}
	
	private static void interactive() throws InterruptedException {
		InteractivePreview preview = new InteractivePreview(renderer, output, 4);
		preview.run();
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		// read args
		String mode = "render-preview";
		if(args.length > 0) mode = args[0];
				
		// set up output objects
		output = new Texture(new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB));
		
		// set up renderer objects
		createRaytracer();
		scene = new Scene9();
		renderer = new Renderer(scene, raytracer, 16, 1024, new ACESTonemapper());
		scene.update(0);

		if(mode.equals("render")) {
			render(false, "output.png");
		} else if(mode.equals("render-preview")) {
			render(true, "output.png");
		} else if(mode.equals("test-animate")) {
			animate();
		} else if(mode.equals("interactive")) {
			interactive();
		} else {
			throw new IllegalArgumentException("Unknown mode.");
		}
		
	}

}