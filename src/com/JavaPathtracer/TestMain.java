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
import com.JavaPathtracer.tonemapping.ReinhardTonemapper;

public class TestMain {
	
	private static Raytracer raytracer;
	private static Renderer renderer;
	private static Scene scene;
	private static BufferedImage output;
	
	private static void animate(int numFrames) throws InterruptedException, IOException {
		
		for(int i = 0; i < numFrames; i++) {
			
			Stopwatch.start("rendering frame " + i);
			
			// animate scene
			scene.update(i);
			
			// render
			RenderJob job = renderer.render(output);
			job.await();
		
			// save
			File file = new File("animation/frame" + i + ".png");
			ImageIO.write(output, "png", file);

			Stopwatch.finish();
			
		}
		
	}
	
	private static void renderWithPreview() throws InterruptedException {
		RenderJob job = renderer.render(output);
		LivePreview preview = new LivePreview(job, 1);
		preview.start();
		job.await();
	}

	private static void launchInteractive() throws InterruptedException {
		RenderJob job = renderer.render(output);
		InteractivePreview preview = new InteractivePreview(job, 4);
		preview.run();
		job.await();
	}
	
	private static void save() throws IOException {
		File file = new File("output.png");
		ImageIO.write(output, "png", file);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {

		/* output */
		output = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
		
		/* scene */
		Stopwatch.start("scene construction");
		scene = new StatueScene();
		Stopwatch.finish();
		
		/* raytracer */
		raytracer = new Pathtracer(8);
		// raytracer = new DebugTracer(Mode.SIMPLE_SHADED);
		
		/* renderer */
		renderer = new Renderer(scene, raytracer, 16, 1, new ReinhardTonemapper());
		// renderer = new Renderer(scene, raytracer, 16, 1, new LinearTonemapper());

		Stopwatch.start("render");
		
		renderWithPreview();
		save();
		
		Stopwatch.finish();
		Stopwatch.cleanup();
	
	}

}