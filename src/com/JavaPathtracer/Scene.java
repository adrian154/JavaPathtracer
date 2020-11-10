package com.JavaPathtracer;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.ISampleable;

public class Scene {

	private List<WorldObject> objects;
	private ISampleable skyEmission;
	
	public Scene() {
		objects = new ArrayList<WorldObject>();
		
		// Default = black sky
		skyEmission = new Vector(0.0, 0.0, 0.0);
	}
	
	public void setSkyEmission(ISampleable newSky) {
		this.skyEmission = newSky;
	}
	
	public Vector getSkyEmission(Vector direction) {
		Vector invDir = new Vector(0.0, 0.0, 0.0).minus(direction);
		double azimuth = (Math.atan2(invDir.z, invDir.x) + 0.5) % (2 * Math.PI);
		double inclination = -Math.asin(invDir.y);
		double u = 0.5 + azimuth / (2 * Math.PI);
		double v = 0.5 - inclination / Math.PI;
		
		return skyEmission.sample(u, v);
	}
	
	public void add(WorldObject object) {
		objects.add(object);
	}
	
	public void add(Shape shape, IMaterial material) {
		this.add(new WorldObject(shape, material));
	}
	
	// do geometry + material trace into scene
	public Hit traceRay(Ray ray) {
		
		// empty constructor Hit has infinite distance which works out for us
		Hit nearest = Hit.MISS;
		for(WorldObject object: objects) {
			
			Hit hit = object.traceRay(ray);
			if(hit.hit && hit.distance < nearest.distance && hit.distance > Raytracer.EPSILON) {
				nearest = hit;
			}
			
		}
		
		return nearest;
		
	}
	
}
