package com.JavaPathtracer.geometry.csg;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;

public class Intersection extends BooleanOperation {

	public Intersection(Shape A, Shape B) {
		super(A, B);
	}

	@Override
	public Hit raytrace(Ray ray) {

		Hit h1 = A.raytrace(ray);
		Hit h2 = B.raytrace(ray);

		if (h1 != null && h2 != null)
			return h2.distance > h1.distance ? h2 : h1;

		// Both rays missed
		return null;

	}

}
