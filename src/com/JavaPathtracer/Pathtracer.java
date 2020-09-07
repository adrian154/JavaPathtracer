package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.material.Texture;

public class Pathtracer extends Raytracer {

	// Maximum depth of bounces
	private int maxLightBounces;
	private int samplesPerPixel;
	
	public Pathtracer(int maxLightBounces, int samplesPerPixel, Camera camera, Scene scene) {
		super(camera, scene);
		this.maxLightBounces = maxLightBounces;
		this.samplesPerPixel = samplesPerPixel;
	}
	
	public int getSamplesPerPixel() {
		return this.samplesPerPixel;
	}
	
	public int getMaxBounces() {
		return this.maxLightBounces;
	}
	
	// scatter diffuse
	public Vector scatterDiffuse(Vector normal) {
		Vector random = Vector.uniformInHemisphere();
		Vector bvx = normal.getOrthagonal();
		Vector bvy = normal;
		Vector bvz = bvy.cross(bvx);
		return Vector.localToWorldCoords(random, bvx, bvy, bvz);
	}
	
	// trace a ray
	public Vector pathtraceRay(Ray ray, int bounces) {
		
		if(bounces > this.maxLightBounces) {
			return new Vector(0.0, 0.0, 0.0);
		}
		
		Hit hit = scene.traceRay(ray);
		if(hit.hit) {
			
			Material mat = hit.hitObject.getMaterial();
			Vector texCoords = hit.textureCoordinates;
			
			// Recursively trace
			Vector diffuseDir = scatterDiffuse(hit.normal);
			Ray nextRay = new Ray(hit.point, diffuseDir);
			
			double factor = hit.normal.dot(diffuseDir);
			Vector recursive = pathtraceRay(nextRay, bounces + 1).times(factor).times(mat.getColor(texCoords.x, texCoords.y));
			
			return mat.getEmission(texCoords.x, texCoords.y).plus(recursive.times(factor));
			
		} else {
			return scene.getSkyEmission(ray.direction);
		}
		
	}
	
	public Vector traceRay(Ray ray) {
		
		Vector result = new Vector();
		for(int i = 0; i < samplesPerPixel; i++)
			result.add(this.pathtraceRay(ray, 0));
		
		return result.divBy(samplesPerPixel);
		
	}
	
	@Override
	public void pathtraceTile(Texture output, int startX, int startY, int endX, int endY) {
		
		double pixelWidth = 2 / output.getWidth();
		double pixelHeight = 2 / output.getHeight();
		
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
					Ray ray = camera.getCameraRay(imageX + pixelWidth * Math.random(), imageY + pixelHeight * Math.random());
					result.add(this.pathtraceRay(ray, 0));
				}
				
				result = result.divBy(samplesPerPixel);

				Vector color = toneMapper.map(result);
				output.set(x, output.getHeight() - y - 1, color);

				
			}
		}
		
	}
	
	// tonemapping
	public double map(double val) {
		return val;
	}
	
}
