package com.JavaPathtracer.scene;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Light;
import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.Material;

public class Scene {

	private List<WorldObject> objects;
	private List<Light> lights;
	private Sky sky;

	public Scene() {
		
		objects = new ArrayList<WorldObject>();
		lights = new ArrayList<Light>();

		// Default = black sky
		sky = new SimpleSky(Vector.ZERO);
		
	}

	public void setSky(Sky sky) {
		this.sky = sky;
	}

	public List<Light> getLights() {
		return this.lights;
	}
	
	public Vector getSkyEmission(Vector direction) {
		return sky.getEmission(direction);
	}

	public void add(WorldObject object) {
		objects.add(object);
	}
	
	public void add(SimpleObject object) {
		this.add((WorldObject)object);
		if(object.getMaterial() instanceof EmissiveMaterial) {
			this.lights.add(new Light((FiniteShape)object.getShape(), (EmissiveMaterial)object.getMaterial()));
		}
	}

	public void add(Shape shape, Material material) {
		this.add(new SimpleObject(shape, material));
	}

	// do geometry + material trace into scene
	public Hit traceRay(Ray ray) {

		// empty constructor Hit has infinite distance which works out for us
		Hit nearest = null;
		for (WorldObject object: objects) {

			Hit hit = object.traceRay(ray);
			if (hit != null && (nearest == null || hit.distance < nearest.distance && hit.distance > Raytracer.EPSILON)) {
				nearest = hit;
			}

		}

		return nearest;

	}

	public Hit traceLightRay(Ray ray) {
		
		Hit nearest = null;
		for(Light light: lights) {
			
			Hit hit = light.intersect(ray);
			if(hit != null && hit.distance < hit.distance && hit.distance > Raytracer.EPSILON) {
				nearest = hit;
			}
			
		}
		
		return nearest;
		
	}
	
	public boolean traceSkyRay(Ray ray) {
		
		for(WorldObject object: objects) {
			if(object.traceRay(ray) != null) {
				return false;
			}
		}
		
		return true;
		
	}
	
	public boolean traceShadowRay(Ray ray, Shape shape) {
		Hit hit = shape.intersect(ray);
		if(hit == null) return true;
		double minDist = hit.distance;
		for(WorldObject object: objects) {
			Hit obstacle = object.traceRay(ray);
			if(obstacle != null && obstacle.distance + Raytracer.EPSILON < minDist) {
				return false;
			}
		}
		return true;
	}

}
