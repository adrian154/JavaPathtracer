package com.JavaPathtracer.material;

import java.util.List;

import com.JavaPathtracer.Light;
import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Scene;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;

public abstract class BRDFMaterial extends BaseMaterial {

	public BRDFMaterial(Sampleable color) {
		super(color);
	}

	// evaluate BRDF
	public abstract double BRDF(Vector incident, Vector outgoing, Vector normal);
	
	// generate reflection vector distributed against BRDF
	public abstract Vector sampleBRDF(Vector incident, Hit hit);
	
	// should light sampling be used?
	// using this in lieu of proper MIS
	public abstract boolean sampleLights();
	
	// ---- light sampling code incoming
	
	public Ray getISRay(Hit hit, Light light) {
		
		Sphere bounding = light.getBoundingSphere();
		Vector toLight = bounding.getCenter().minus(hit.point).normalize();
		
		double angle = Math.random() * 2 * Math.PI;
		Vector randomOnDisc = new Vector(Math.cos(angle), 0, Math.sin(angle)).imul(Math.random() * bounding.getRadius());
		Vector bvx = toLight.getOrthagonal();
		Vector bvy = toLight;
		Vector bvz = bvx.cross(bvy);
		Vector worldPos = bounding.getCenter().plus(Vector.localToWorldCoords(randomOnDisc, bvx, bvy, bvz));

		return new Ray(hit.point, worldPos.minus(hit.point).normalized());
		
	}
	
	public Vector sampleLights(Vector incident, Hit hit, Scene scene) {
		
		Vector sum = new Vector();
		List<Light> lights = scene.getLights();
		
		for(Light light: lights) {

			Sphere bounding = light.getBoundingSphere();
			Ray ray = getISRay(hit, light);
			
			if(scene.traceShadowRay(ray, light)) {

				double dist = bounding.getCenter().minus(hit.point).length();
				double hypot = Math.sqrt(dist * dist + bounding.getRadius() * bounding.getRadius());
				double solidAngle = 2 * Math.PI * (1 - dist / hypot);
				Vector irradiance = light.material.color.sample(hit.textureCoordinates.x, hit.textureCoordinates.y).times(solidAngle / (2 * Math.PI));

				double cosFactor = ray.direction.dot(hit.normal);
				sum.iadd(irradiance.imul(cosFactor).imul(BRDF(incident, ray.direction, hit.normal)));
				
			}
			
		}
		
		return sum;
		
	}
	
	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Scene scene, Pathtracer pathtracer) {
		
		boolean samplingLights = sampleLights();
		
		Ray next = new Ray(hit.point, sampleBRDF(incident, hit));
		Vector result = pathtracer.pathtraceRay(scene, next, bounces + 1, !samplingLights).times(next.direction.dot(hit.normal));
		
		if(samplingLights) {
			result.iadd(sampleLights(incident, hit, scene));
		}
		
		Vector color = this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
		return result.times(color);
		
	}
	
}