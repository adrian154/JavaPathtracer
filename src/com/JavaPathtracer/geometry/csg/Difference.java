package com.JavaPathtracer.geometry.csg;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;

public class Difference extends BooleanOperation {

	// Shape B is carved out of shape A.
	public Difference(Shape A, Shape B) {
		super(A, B);
	}

	// TODO: figure out what the hell i was doing thinking writing this
	@Override
	public Hit raytrace(Ray ray) {

		boolean inA = false, inB = false;

		while (true) {

			Hit h1 = A.raytrace(ray);
			Hit h2 = B.raytrace(ray);

			if (h1.distance < h2.distance) {
				inA = !inA;
				ray.origin = h1.point;
			} else if (h2.hit) {
				inB = !inB;
				ray.origin = h2.point;
			} else {
				return Hit.MISS;
			}

			if (inA && !inB) {
				// TODO: move normal reversal
				if (h1.normal.dot(ray.direction) > 0) {
					h1.normal = h1.normal.reverse();
				}
				return h1;
			}

		}

	}

}
