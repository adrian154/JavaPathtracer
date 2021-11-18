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
	protected List<WorldObject> lights;
	protected Camera camera;
	private Sky sky;
	
	public Scene() {
		
		objects = new ArrayList<WorldObject>();
		lights = new ArrayList<WorldObject>();

		// Default = black sky
		sky = new SimpleSky(Vector.ZERO);
		
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
	
	public List<WorldObject> getLights() {
		return this.lights;
	}
	
	public void add(WorldObject object) {
		objects.add(object);
	}
	
	public void add(SimpleObject object) {
		this.add(object);
		if(object.getMaterial().shouldImportanceSample()) {
			this.lights.add(object);
		}
	}

	public void add(Shape shape, Material material) {
		this.add(new SimpleObject(shape, material));
	}

	// do geometry + material trace into scene
	// `target` is useful for shadow ray type stuff
	public Hit traceRay(Ray ray, WorldObject target) {

		Hit nearest = null;
		WorldObject nearestObj = null;
		
		for (WorldObject object: objects) {
			
			Hit hit = object.traceRay(ray);
			if (hit != null && (nearest == null || hit.distance < nearest.distance && hit.distance > Raytracer.EPSILON)) {
				nearest = hit;
				nearestObj = object;
			}

		}

		if(target != null && target != nearestObj) return null;
		return nearest;

	}

	public Hit traceRay(Ray ray) {
		return this.traceRay(ray, null);
	}

	public boolean traceSkyRay(Ray ray) {
		
		for(WorldObject object: objects) {
			if(object.traceRay(ray) != null) {
				return false;
			}
		}
		
		return true;
		
	}

	public void update(int frame) {
		
	}

}
