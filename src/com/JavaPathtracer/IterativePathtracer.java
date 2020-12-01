package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.tonemapping.IToneMapper;

public class IterativePathtracer extends Pathtracer {

	private Vector[][] image;

	public IterativePathtracer(Texture output, int maxLightBounces, Camera camera, Scene scene,
			IToneMapper toneMapper) {
		super(maxLightBounces, 0, camera, scene, toneMapper);
		image = new Vector[output.getWidth()][output.getHeight()];
		for (int x = 0; x < image.length; x++) {
			for (int y = 0; y < image[x].length; y++) {
				image[x][y] = new Vector();
			}
		}
	}

	public void pathtraceTile(Texture output, int startX, int startY, int endX, int endY, int iteration) {

		double pixelWidth = 2.0 / output.getWidth();
		double pixelHeight = 2.0 / output.getHeight();

		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {

				// set pixel to green while working on it
				output.set(x, output.getHeight() - y - 1, new Vector(0, 1, 0));

				// convert to image plane coordinates
				double imageX = ((double) x / output.getWidth()) * 2 - 1;
				double imageY = ((double) y / output.getHeight()) * 2 - 1;

				// apply jitter
				Ray ray = camera.getCameraRay(imageX + pixelWidth * Math.random() - pixelWidth / 2,
						imageY + pixelHeight * Math.random() - pixelHeight / 2);
				Vector iter = this.pathtraceRay(ray, 0);
				image[x][y].iadd(iter);

				Vector color = toneMapper.map(image[x][y].divBy(iteration));
				output.set(x, output.getHeight() - y - 1, color);

			}
		}

	}

}
