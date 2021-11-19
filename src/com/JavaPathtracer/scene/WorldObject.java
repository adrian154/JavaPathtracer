package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;

public interface WorldObject {

	public ObjectHit traceRay(Ray ray);
	public BoundingBox getBoundingBox();

}
