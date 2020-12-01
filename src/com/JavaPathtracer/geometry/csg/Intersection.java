package com.JavaPathtracer.geometry.csg;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;

public class Intersection extends BooleanOperation {

	public Intersection(Shape A, Shape B) {
		super(A, B);
	}

	@Override
	public Hit intersect(Ray ray) {

		Hit h1 = A.intersect(ray);
		Hit h2 = B.intersect(ray);

		if (h1.hit && h2.hit)
			return h2.distance > h1.distance ? h2 : h1;

		// Both rays missed
		return Hit.MISS;

	}

}
