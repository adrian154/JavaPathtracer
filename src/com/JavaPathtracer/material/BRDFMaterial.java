package com.JavaPathtracer.material;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.WorldObject;

public abstract class BRDFMaterial extends BaseMaterial {

	private boolean sampleLights;
	
	public BRDFMaterial(Sampleable color, boolean sampleLights) {
		super(color);
		this.sampleLights = sampleLights;
	}

	// BRDF normalization must be handled inside this function
	protected abstract double BRDF(Hit hit, Vector outgoing);
	
	// TODO: use cosine weighted or low-discrepancy sampling
	protected Vector sample(Hit hit) {	
		Vector random = Vector.uniformInHemisphere();
		//Vector random = Vector.fromSpherical(Math.random() * 2 * Math.PI, Math.random() * Math.PI / 2);
		Vector bvx = hit.normal.getOrthagonal();
		Vector bvy = hit.normal;
		Vector bvz = bvy.cross(bvx);
		return random.fromCoordinateSpace(bvx, bvy, bvz);
	}
	
	// PDF of sample() (which might not draw from the BRDF)
	// default: uniform random sampling
	// the PDF should integrate to 1 over a hemisphere
	protected double samplerPDF(Hit hit, Vector direction) {
		return 1 / (2 * Math.PI);
		//return direction.dot(hit.normal) / Math.PI;
	}
	
	// in lieu of proper MIS
	protected boolean shouldSampleLights() {
		return true;
	}
	
	// ---- mathy code incoming
	
	// generate a ray towards the light sample
	// TODO: implement solid angle sampling and destroy this hideous abomination
	private Ray generateLightSample(Hit hit, WorldObject object) {
		
		// get the sphere which surrounds the light
		Sphere bounding = object.getBoundingBox().toSphere();
		Vector towardsOrigin = bounding.center.minus(hit.point).normalize();
		
		// we pick points to direct our light samples towards by generating a random point on a great circle of the light's bounding sphere pointing towards the ray origin
		// pick random point on disc by picking a random rotation and distance from center
		double rotation = ThreadLocalRandom.current().nextDouble() * 2 * Math.PI;
		
		// generate offset vector on the disc, which exists in the plane of the disc
		Vector randomOnDisc = new Vector(Math.cos(rotation), 0, Math.sin(rotation)).times(ThreadLocalRandom.current().nextDouble() * bounding.radius);
		
		// transform the disc vector to world space
		Vector bvx = towardsOrigin.getOrthagonal();
		Vector bvy = towardsOrigin;
		Vector bvz = bvx.cross(bvy);
		
		// offset from the center of the disc to get the final point
		Vector worldPos = bounding.center.plus(randomOnDisc.fromCoordinateSpace(bvx, bvy, bvz));

		// generate new ray pointing at our point
		return new Ray(hit.point, worldPos.minus(hit.point).normalize());
		
	}

	// solid angle = 2pi(1 - cos(alpha)) where alpha = angle between disc center, edge as seen by hitpoint
	private static double discSolidAngle(double radius, double distance) {
		return 2 * Math.PI * (1 - distance / Math.sqrt(distance * distance + radius * radius));
	}
	
	private Vector sampleLights(Hit hit, Scene scene, Pathtracer pathtracer, int bounces, double ior) {
		
		// for each light...
		Vector sum = new Vector();
		List<WorldObject> lights = scene.getLights();
		
		for(WorldObject light: lights) {

			// get importance-generated ray
			Sphere bounding = light.getBoundingBox().toSphere();
			Ray ray = generateLightSample(hit, light);
			
			ObjectHit lightHit = scene.traceRay(ray, light);
			
			if(lightHit.hit) {

				// area sampling means our BRDF is just (angle sampled) / (area of unit hemisphere)
				double solidAngle = discSolidAngle(bounding.radius, bounding.center.minus(hit.point).length());
				Vector irradiance = lightHit.material.shade(lightHit, bounces + 1, scene, pathtracer, ior).times(solidAngle / (2 * Math.PI));
				
				double cosFactor = ray.direction.dot(hit.normal);
				sum = sum.plus(irradiance.times(cosFactor).times(BRDF(hit, ray.direction)));
			
			}
			
		}
		
		return sum;
		
	}
	
	private Vector sampleBRDF(Hit hit, Scene scene, Pathtracer pathtracer, int bounces, double ior, boolean excludeLights) {
		Ray ray = new Ray(hit.point, this.sample(hit));
		double samplePDF = samplerPDF(hit, ray.direction);
		double brdf = BRDF(hit, ray.direction);
		return pathtracer.pathtraceRay(scene, ray, bounces + 1, ior, excludeLights).times(brdf / samplePDF * ray.direction.dot(hit.normal));
	}
	
	@Override
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double ior) {
		
		// don't increment bounces twice
		// if sampling lights, exclude lights from the BRDF contribution
		Vector radiance = this.sampleBRDF(hit, scene, pathtracer, bounces, ior, this.sampleLights);
		
		if(this.sampleLights) {
			radiance = radiance.plus(this.sampleLights(hit, scene, pathtracer, bounces, ior));
		}
		
		Vector color = this.getColor(hit.textureCoord);
		return radiance.times(color);
		
	}
	
	// don't direct light importance samples towards BRDF materials since they probably don't emit light
	@Override
	public boolean shouldImportanceSample() {
		return false;
	}
	
}