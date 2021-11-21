package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Transform;

public abstract class WorldObject {

	protected Transform transform;
	
	public WorldObject(Transform transform) {
		this.transform = transform;
	}
	
	public ObjectHit traceRay(Ray ray) {
		
		// keep track of the original ray if it needs to be transformed
		Ray originalRay = null;
		if(transform != null) {
			originalRay = ray;
			ray = new Ray(transform.inversePoint(ray.origin), transform.inverseVector(ray.direction));
		}
		
		ObjectHit hit = this.raytraceObject(ray);
		
		if(transform != null) {
			hit.point = transform.transformPoint(hit.point);
			hit.normal = transform.transformNormal(hit.normal);
			hit.tangent = transform.transformVector(hit.tangent);
			hit.distance = hit.point.minus(ray.origin).length();
			hit.ray = originalRay;
		}
		
		return hit;
		
	}
	
	public abstract ObjectHit raytraceObject(Ray ray);
	public abstract BoundingBox getBoundingBox();

}
