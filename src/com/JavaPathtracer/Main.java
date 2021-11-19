package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.renderer.InteractivePreview;
import com.JavaPathtracer.renderer.LivePreview;
import com.JavaPathtracer.renderer.Renderer;
import com.JavaPathtracer.renderer.Renderer.RenderJob;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.testscenes.Scene6;
import com.JavaPathtracer.tonemapping.LinearTonemapper;

public class Main {
		
	private static Raytracer createRaytracer() {
		return new Pathtracer(5);
		//return new DebugTracer(Mode.SIMPLE_SHADED);
	}
	
	private static Renderer createRenderer(Scene scene, Raytracer raytracer) {
		return new Renderer(scene, raytracer, 16, 256, new LinearTonemapper());
	}
	
	private static void render(boolean preview, String outputName) throws InterruptedException, IOException {
		RenderJob job = renderer.render(output);
		if(preview) {
			LivePreview previewObj = new LivePreview(job, 1);
			previewObj.start();
		}
		job.await();
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
		BufferedImage output = new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB);
		
		// set up renderer objects
		Raytracer raytracer = createRaytracer();
		Scene scene = new Scene6();
		Renderer renderer = createRenderer(scene, raytracer);

		// render
		RenderJob job = renderer.render(output);
		if(mode.equals("preview")) {
			LivePreview preview = new LivePreview(job, 1);
		} else if(mode.equals("animate")) {
			for(int i = 0; i < 360; i++) {
				scene.update(i);
				// TODO: finish this
			}
		} else if(mode.equals("interactive")) {
			interactive();
		} else {
			throw new IllegalArgumentException("Unknown mode.");
		}
		
	}

}