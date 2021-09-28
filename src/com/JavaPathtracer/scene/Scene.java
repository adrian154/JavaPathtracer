package com.JavaPathtracer.scene;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.material.Material;

public class Scene {

	protected List<WorldObject> objects;
	protected Camera camera;
	
	public Scene() {
		objects = new ArrayList<WorldObject>();
		this.camera = this.createCamera();
	}
	
	protected Camera createCamera() {
		return new PerspectiveCamera();
	}
	
	// implemented by subclasses for animation
	public void update(int frame) { }

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public void add(WorldObject object) {
		objects.add(object);
	}

	public void add(Shape shape, Material material) {
		this.add(new WorldObject(shape, material));
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

}
