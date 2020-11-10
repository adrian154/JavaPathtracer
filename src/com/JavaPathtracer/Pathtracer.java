package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.Texture;
import com.JavaPathtracer.tonemapping.IToneMapper;

public class Pathtracer extends Raytracer {

	// Maximum depth of bounces
	private int maxLightBounces;
	private int samplesPerPixel;
	
	public Pathtracer(int maxLightBounces, int samplesPerPixel, Camera camera, Scene scene) {
		super(camera, scene);
		this.maxLightBounces = maxLightBounces;
		this.samplesPerPixel = samplesPerPixel;
	}
	
	public Pathtracer(int maxLightBounces, int samplesPerPixel, Camera camera, Scene scene, IToneMapper toneMapper) {
		this(maxLightBounces, samplesPerPixel, camera, scene);
		this.toneMapper = toneMapper;
	}
	
	public int getSamplesPerPixel() {
		return this.samplesPerPixel;
	}
	
	public int getMaxBounces() {
		return this.maxLightBounces;
	}

	// trace a ray
	public Vector pathtraceRay(Ray ray, int bounces) {
		
		if(bounces > this.maxLightBounces) {
			return new Vector(0.0, 0.0, 0.0);
		}
		
		Hit hit = scene.traceRay(ray);
		if(hit.hit) {
			
			IMaterial mat = hit.hitObject.getMaterial();
			Vector texCoords = hit.textureCoordinates;
			
			// Recursively trace
			//Vector nextDir = scatterDiffuse(hit.normal);
			Vector nextDir = mat.scatter(texCoords.x, texCoords.y, ray.direction, hit.normal);
			Ray nextRay = new Ray(hit.point, nextDir);
			
			Vector recursive = pathtraceRay(nextRay, bounces + 1).times(mat.getColor(texCoords.x, texCoords.y));
			if(mat.doDotProduct(texCoords.x, texCoords.y)) {
				recursive.imul(hit.normal.dot(nextDir));
			}
			
			return mat.getEmission(texCoords.x, texCoords.y).plus(recursive);
			
		} else {
			return scene.getSkyEmission(ray.direction);
		}
		
	}
	
	public Vector traceRay(Ray ray) {
		
		Vector result = new Vector();
		for(int i = 0; i < samplesPerPixel; i++)
			result.iadd(this.pathtraceRay(ray, 0));
		
		return result.idiv(samplesPerPixel);
		
	}
	
	@Override
	public void pathtraceTile(Texture output, int startX, int startY, int endX, int endY) {
		
		double pixelWidth = 2.0 / output.getWidth();
		double pixelHeight = 2.0 / output.getHeight();
		
		for(int x = startX; x < endX; x++) {
			for(int y = startY; y < endY; y++) {
				
				// set pixel to green while working on it
				output.set(x, output.getHeight() - y - 1, new Vector(0, 1, 0));
				
				// convert to image plane coordinates
				double imageX = ((double)x / output.getWidth()) * 2 - 1;
				double imageY = ((double)y / output.getHeight()) * 2 - 1;
				
				// apply jitter
				Vector result = new Vector();
				for(int i = 0; i < samplesPerPixel; i++) {
					Ray ray = camera.getCameraRay(imageX + pixelWidth * Math.random() - pixelWidth / 2, imageY + pixelHeight * Math.random() - pixelHeight / 2);
					result.iadd(this.pathtraceRay(ray, 0));
				}
				
				result.idiv(samplesPerPixel);

				Vector color = toneMapper.map(result);
				output.set(x, output.getHeight() - y - 1, color);
				
			}
		}
		
	}

}
