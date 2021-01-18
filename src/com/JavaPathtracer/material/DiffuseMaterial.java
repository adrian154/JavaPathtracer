package com.JavaPathtracer.material;

import java.util.List;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Scene;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;

public class DiffuseMaterial extends BaseMaterial {

	public DiffuseMaterial(ISampleable color) {
		super(color);
	}

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
	
	public Vector importanceSampleLights(Hit hit, Scene scene) {
		
		Vector sum = new Vector();
		List<Light> lights = scene.getLights();
		for(Light light: lights) {
			Sphere bounding = light.getBoundingSphere();
			Ray ray = getISRay(hit, light);
			if(scene.traceShadowRay(ray, light)) {
				double dist = bounding.getCenter().minus(hit.point).length();
				double hypot = Math.sqrt(dist * dist + bounding.getRadius() * bounding.getRadius());
				double solidAngle = 2 * Math.PI * (1 - dist / hypot);
				//System.out.println("solid angle = " + solidAngle);
				sum.iadd(light.color.times(ray.direction.dot(hit.normal)).times(solidAngle / (2 * Math.PI)));
			}
		}
		
		return sum;
		
	}
	
	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Scene scene, Pathtracer pathtracer) {

		Vector random = Vector.uniformInHemisphere();
		Vector bvx = hit.normal.getOrthagonal();
		Vector bvy = hit.normal;
		Vector bvz = bvy.cross(bvx);

		Vector dir = Vector.localToWorldCoords(random, bvx, bvy, bvz);
		Ray next = new Ray(hit.point, dir);

		Vector color = this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
		Vector indirect = pathtracer.pathtraceRay(scene, next, bounces + 1).times(color).times(dir.dot(hit.normal));
		Vector direct = importanceSampleLights(hit, scene).times(color);
		return indirect.iadd(direct);

	}

}
