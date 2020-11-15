package com.JavaPathtracer.geometry.csg;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;

public class Difference extends BooleanOperation {

	public Difference(Shape A, Shape B) {
		super(A, B);
	}
	
	@Override
	public Hit intersect(Ray ray) {
		
		boolean inA = false, inB = false;
		
		while(true) {
			
			Hit h1 = A.intersect(ray);
			Hit h2 = B.intersect(ray);
			
			if(h1.hit && h1.distance < h2.distance) {
				inA = !inA;
				ray.origin = h1.point;
			} else if (h2.hit) {
				inB = !inB;
				ray.origin = h2.point;
			} else {
				return Hit.MISS;
			}
			
			if(inA && !inB) {
				if(h1.normal.dot(ray.direction) > 0) {
					h1.normal.invert();
				}
				return h1;
			}
			
		}
		
	}
	
}
