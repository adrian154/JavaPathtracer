package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;

// Q: How is this different from Shape?
// A: The Hit's returned from this thing should have material data
public interface WorldObject {
	public Hit traceRay(Ray ray);
}
