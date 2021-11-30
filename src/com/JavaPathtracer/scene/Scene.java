package com.JavaPathtracer.scene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;

public class Scene {

	protected List<WorldObject> objects;
	protected List<WorldObject> lights;
	protected Camera camera;
	private Environment environment;
	
	public Scene() throws IOException {
		
		objects = new ArrayList<WorldObject>();
		lights = new ArrayList<WorldObject>();
		
		this.camera = this.createCamera();
		this.environment = this.createEnvironment();
		this.update(0);
		
	}
	
	// default setup methods
	protected Camera createCamera() {
		return new PerspectiveCamera();
	}
	
	protected Environment createEnvironment() throws IOException {
		return new SimpleSky(Vector.ONE);
	}

	// getters
	public Camera getCamera() {
		return this.camera;
	}
	
	public Environment getSky() {
		return environment;
	}
	
	public List<WorldObject> getLights() {
		return this.lights;
	}
	
	// TODO: automatically detect if meshes are lights
	public void add(WorldObject object) {
		add(object, false);
	}
	
	public void add(WorldObject object, boolean isLight) {
		objects.add(object);
		if(isLight) {
			this.lights.add(object);
		}
	}
	
	public void add(SimpleObject object) {
		this.add(object, object.material.shouldImportanceSample());
	}

	public void add(Shape shape, Material material) {
		this.add(new SimpleObject(shape, material));
	}
	
	// trace ray into the scene
	public ObjectHit traceRay(Ray ray, boolean excludeLights) {
	
		ObjectHit nearest = ObjectHit.MISS;
		
		for(WorldObject object: objects) {
			
			ObjectHit hit = object.raytrace(ray);
			if(hit.distance < nearest.distance && hit.distance > Pathtracer.EPSILON) {
				nearest = hit;
			}
			
		}
		
		return nearest;
		
	}
	
	
	// visibility test (null for sky)
	public ObjectHit traceRay(Ray ray, WorldObject target) {

		ObjectHit desiredHit = target == null ? ObjectHit.MISS : target.raytrace(ray);
		if(!desiredHit.hit) {
			return ObjectHit.MISS;
		}
		
		for (WorldObject object: objects) {
			
			if(object == target) continue;
			
			ObjectHit hit = object.raytrace(ray);
			if (hit.distance > Pathtracer.EPSILON && hit.distance < desiredHit.distance) {
				return ObjectHit.MISS;
			}

		}

		// There's nothing in the way.
		return desiredHit;
		
	}

	public void update(int frame) {
		
	}

}
