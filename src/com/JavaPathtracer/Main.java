package com.JavaPathtracer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JavaPathtracer.renderer.InteractivePreview;
import com.JavaPathtracer.renderer.LivePreview;
import com.JavaPathtracer.renderer.Renderer;
import com.JavaPathtracer.renderer.Renderer.RenderJob;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scenes.StatueScene;
import com.JavaPathtracer.tonemapping.FilmicTonemapper;

public class Main {
		
	private static Raytracer createRaytracer() {
		return new Pathtracer(8);
		//return new DebugTracer(Mode.SIMPLE_SHADED);
	}
	
	private static Renderer createRenderer(Scene scene, Raytracer raytracer) {
		//return new Renderer(scene, raytracer, 16, 1, new LinearTonemapper());
		return new Renderer(scene, raytracer, 16, 1, new FilmicTonemapper());
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		// read args
		String mode = "preview";
		if(args.length > 0) mode = args[0];
				
		// set up output objects
		BufferedImage output = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		
		// set up renderer objects
		Raytracer raytracer = createRaytracer();
		Scene scene = new StatueScene();
		Renderer renderer = createRenderer(scene, raytracer);

		if(mode.equals("animate")) {
			
			int numFrames = 360;
			if(args.length > 1) numFrames = Integer.parseInt(args[1]);
			for(int i = 0; i < numFrames; i++) {
				
				// render
				scene.update(i);
				RenderJob job = renderer.render(output);
				job.await();
				
				// save
				File file = new File("animation/frame" + i + ".png");
				ImageIO.write(output, "png", file);
				System.out.printf("Finished frame %d of %d\n", i, numFrames);
				
			}
			
		} else {
			
			// render
			long start = System.currentTimeMillis();
			RenderJob job = renderer.render(output);
	
			// create various windows
			if(mode.equals("preview")) {
				LivePreview preview = new LivePreview(job, 2);
				preview.start();
			} else if(mode.equals("interactive")) {
				InteractivePreview preview = new InteractivePreview(job, 4);
				preview.run();
			} else {
				throw new RuntimeException("Unknown mode: " + mode);
			}
			
			// save image
			job.await();
			long finish = System.currentTimeMillis();
			System.out.println(finish-start);
			File file = new File("output.png");
			ImageIO.write(output, "png", file);
			
		}
		
	}

}