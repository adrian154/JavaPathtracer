package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.tonemapping.LinearTonemapper;
import com.JavaPathtracer.tonemapping.IToneMapper;

public abstract class Raytracer {

	public static final double EPSILON = 0.000001;

	// Camera and scene
	protected Camera camera;
	protected Scene scene;
	protected IToneMapper toneMapper;

	public Raytracer(Camera camera, Scene scene) {
		this(camera, scene, new LinearTonemapper());
	}

	public Raytracer(Camera camera, Scene scene, IToneMapper toneMapper) {
		this.camera = camera;
		this.scene = scene;
		this.toneMapper = toneMapper;
	}

	public Camera getCamera() {
		return camera;
	}

	// shade a normal for debugging
	public static Vector shadeNormal(Vector normal) {
		return normal.times(0.5).plus(new Vector(0.5, 0.5, 0.5));
	}

	public abstract Vector traceRay(Ray ray);

	public void pathtraceTile(Texture output, int startX, int startY, int endX, int endY) {

		int maxDim = Math.min(output.getWidth(), output.getHeight());
		
		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {

				// set pixel to green while working on it
				output.set(x, output.getHeight() - y - 1, new Vector(0, 1, 0));

				// convert to image plane coordinates
				double imageX = ((double) x / maxDim) * 2 - 1;
				double imageY = ((double) y / maxDim) * 2 - 1;

				// apply jitter
				Ray ray = camera.getCameraRay(imageX, imageY);

				Vector color = toneMapper.map(traceRay(ray));
				output.set(x, output.getHeight() - y - 1, color);

			}
		}

	}

}
