package com.JavaPathtracer;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.IMaterial;
import com.JavaPathtracer.material.Sampleable;

public class Scene {

	private List<WorldObject> objects;
	private List<Light> lights;
	private Sampleable skyEmission;

	public Scene() {
		objects = new ArrayList<WorldObject>();
		lights = new ArrayList<Light>();

		// Default = black sky
		skyEmission = new Vector(0.0, 0.0, 0.0);
	}

	public void setSkyEmission(Sampleable newSky) {
		this.skyEmission = newSky;
	}

	//private static final Vector SKY_DIR = new Vector(0.8, 1.0, -1.0).normalized();
	
	public List<Light> getLights() {
		return this.lights;
	}
	
	public Vector getSkyEmission(Vector direction) {
		Vector invDir = new Vector(0.0, 0.0, 0.0).minus(direction);
		double azimuth = (Math.atan2(invDir.z, invDir.x) + 0.5) % (2 * Math.PI);
		double inclination = Math.asin(invDir.y);
		double u = 0.5 + azimuth / (2 * Math.PI);
		double v = 0.5 - inclination / Math.PI;
		return skyEmission.sample(u, v);
		//return direction.dot(SKY_DIR) < 0 ? new Vector(10.0, 0.0, 0.0) : new Vector();
	}

	public void add(WorldObject object) {
		objects.add(object);
		if(object.getMaterial() instanceof EmissiveMaterial && object.getShape() instanceof FiniteShape) {
			this.lights.add(new Light((FiniteShape)object.getShape(), (EmissiveMaterial)object.getMaterial()));
		}
	}

	public void add(Shape shape, IMaterial material) {
		this.add(new WorldObject(shape, material));
	}

	// do geometry + material trace into scene
	public Hit traceRay(Ray ray) {

		// empty constructor Hit has infinite distance which works out for us
		Hit nearest = Hit.MISS;
		for (WorldObject object : objects) {

			Hit hit = object.traceRay(ray);
			if (hit.hit && hit.distance < nearest.distance && hit.distance > Raytracer.EPSILON) {
				nearest = hit;
			}

		}

		return nearest;

	}

	public Hit traceLightRay(Ray ray) {
		
		Hit nearest = Hit.MISS;
		for(Light light: lights) {
			
			Hit hit = light.intersect(ray);
			if(hit.hit && hit.distance < hit.distance && hit.distance > Raytracer.EPSILON) {
				nearest = hit;
			}
			
		}
		
		return nearest;
		
	}
	
	public boolean traceSkyRay(Ray ray) {
		
		for(WorldObject object: objects) {
			if(object.traceRay(ray).hit) {
				return false;
			}
		}
		
		return true;
		
	}
	
	public boolean traceShadowRay(Ray ray, Shape shape) {
		double minDist = shape.intersect(ray).distance;
		for(WorldObject object: objects) {
			Hit hit = object.traceRay(ray);
			if(hit.distance < minDist + Raytracer.EPSILON) {
				return false;
			}
		}
		return true;
	}

}
