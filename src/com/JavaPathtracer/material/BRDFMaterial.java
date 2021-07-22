package com.JavaPathtracer.material;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.Light;
import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.Sun;

public abstract class BRDFMaterial extends BaseMaterial {

	public BRDFMaterial(Sampleable color) {
		super(color);
	}

	// BRDF
	public abstract double BRDF(Vector incident, Vector outgoing, Vector normal, Vector textureCoordinates);
	
	// sampler for integration
	// does not have to necessarily sample the BRDF, hence a separate PDF() method
	// PDF() defaults to 1 since sample() generally samples the BRDF
	public abstract Vector sample(Vector incident, Hit hit);
	
	public double PDF(Vector incident, Vector outgoing, Vector normal) {
		return 1;
	}
	
	// should light sampling be used?
	// using this in lieu of proper MIS
	public abstract boolean sampleLights();
	
	// ---- mathy code incoming
	
	public Ray getISRay(Hit hit, Light light) {
		
		Sphere bounding = light.getBoundingSphere();
		
		// get direction towards light
		Vector toLight = bounding.getCenter().minus(hit.point).normalize();
		
		// pick random angle
		double angle = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
		
		// generate a random vector on the disc
		Vector randomOnDisc = new Vector(Math.cos(angle), 0, Math.sin(angle)).imul(ThreadLocalRandom.current().nextDouble() * bounding.getRadius());
		
		// transform that random disc vector into a coordinate space whose XZ plane is in the plane of the disc
		Vector bvx = toLight.getOrthagonal();
		Vector bvy = toLight;
		Vector bvz = bvx.cross(bvy);
		
		// get world position
		Vector worldPos = bounding.getCenter().plus(Vector.localToWorldCoords(randomOnDisc, bvx, bvy, bvz));

		// return ray
		return new Ray(hit.point, worldPos.minus(hit.point).normalized());
		
	}
	
	public Vector sampleLights(Hit hit, Scene scene) {
		
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
				sum.iadd(irradiance.imul(cosFactor).imul(BRDF(hit.ray.direction.reversed(), ray.direction, hit.normal, hit.textureCoordinates)));
				
			}
			
		}
		
		return sum;
		
	}

	public Vector sampleSun(Hit hit, Scene scene) {
		
		Sun sun = scene.getSun();
		
		if(sun == null) return Vector.ZERO;
		
		// dirty, but works..
		double dist = Math.random() * sun.radius;
		double angle = Math.random() * 2 * Math.PI;
		Vector inPlane = new Vector(Math.cos(angle) * dist, 0, Math.sin(angle) * dist);
	
		Vector bvx = sun.direction.getOrthagonal().normalize();
		Vector bvz = sun.direction.cross(bvx);
		Vector dir = sun.direction.plus(Vector.localToWorldCoords(inPlane, bvx, sun.direction, bvz)).normalize();
		
		Ray ray = new Ray(hit.point, dir);
		if(scene.traceSkyRay(ray)) {
			return sun.color.times(dir.dot(hit.normal)).imul(BRDF(hit.ray.direction.reversed(), dir, hit.normal, hit.textureCoordinates));
		}
		
		return Vector.ZERO;
		
	}
	
	@Override
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double ior) {
		
		boolean samplingLights = sampleLights();

		Ray next = new Ray(hit.point, sample(hit.ray.direction, hit));
		Vector recursive = pathtracer.pathtraceRay(scene, next, bounces + 1, !samplingLights, ior);
		Vector result = recursive.times(BRDF(hit.ray.direction.reversed(), next.direction, hit.normal, hit.textureCoordinates) / PDF(hit.ray.direction, next.direction, hit.normal)).times(next.direction.dot(hit.normal));

		if(samplingLights) {
			result.iadd(sampleLights(hit, scene));
			result.iadd(sampleSun(hit, scene));
		}
		
		Vector color = this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
		return result.times(color);
		
	}
	
}