package com.JavaPathtracer.material;

import java.util.List;

import com.JavaPathtracer.Light;
import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

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
	
	// ---- mathy code incoming
	
	public Ray getISRay(Hit hit, Light light) {
		
		Sphere bounding = light.getBoundingSphere();
		
		// get direction towards light
		Vector toLight = bounding.getCenter().minus(hit.point).normalize();
		
		// pick random angle
		double angle = Math.random() * 2 * Math.PI;
		
		// generate a random vector on the disc
		Vector randomOnDisc = new Vector(Math.cos(angle), 0, Math.sin(angle)).imul(Math.random() * bounding.getRadius());
		
		// transform that random disc vector into a coordinate space whose XZ plane is in the plane of the disc
		Vector bvx = toLight.getOrthagonal();
		Vector bvy = toLight;
		Vector bvz = bvx.cross(bvy);
		
		// get world position
		Vector worldPos = bounding.getCenter().plus(Vector.localToWorldCoords(randomOnDisc, bvx, bvy, bvz));

		// return ray
		return new Ray(hit.point, worldPos.minus(hit.point).normalized());
		
	}
	
	public Vector sampleLights(Vector incident, Hit hit, Scene scene) {
		
		// for each light...
		Vector sum = new Vector();
		List<Light> lights = scene.getLights();
		
		for(Light light: lights) {

			// get importance-generated ray
			Sphere bounding = light.getBoundingSphere();
			Ray ray = getISRay(hit, light);
			
			if(scene.traceShadowRay(ray, light)) {

				// calculate solid angle
				// solid angle = 2pi(1 - cos(alpha)) where alpha = angle between disc center, edge as seen by hitpoint
				// avoid the need for an expensive arctangent
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
		Vector result = pathtracer.pathtraceRay(scene, next, bounces + 1, !samplingLights).times(BRDF(incident, next.direction, hit.normal)).times(next.direction.dot(hit.normal));
		
		if(samplingLights) {
			result.iadd(sampleLights(incident, hit, scene));
		}
		
		Vector color = this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
		return result.times(color);
		
	}
	
}