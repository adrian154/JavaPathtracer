package com.JavaPathtracer.scene;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Light;
import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.Material;

public class Scene {

	protected List<WorldObject> objects;
	protected List<Light> lights;
	protected Camera camera;
	private Sky sky;
	private Sun sun;
	
	public Scene() {
		
		objects = new ArrayList<WorldObject>();
		lights = new ArrayList<Light>();

		// Default = black sky
		sky = new SimpleSky(Vector.ZERO);
		sun = null;
		
		this.camera = this.createCamera();
		
	}
	
	public Camera createCamera() {
		return new PerspectiveCamera();
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Camera getCamera() {
		return this.camera;
	}
	
	public void setSky(Sky sky) {
		this.sky = sky;
	}
	
	public void setSun(Sun sun) {
		this.sun = sun;
	}

	public Sun getSun() {
		return sun;
	}
	
	public List<Light> getLights() {
		return this.lights;
	}
	
	public Vector getSkyEmission(Vector direction, boolean allowSun) {
		if(allowSun && sun != null && direction.dot(sun.direction) > sun.cosAngularRadius) {
			return sun.color;
		}
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
	
	public void update(int frame) {
		
	}

}
