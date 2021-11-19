package com.JavaPathtracer.geometry.csg;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Shape;

public abstract class BooleanOperation implements Shape {

	protected Shape A, B;

	public BooleanOperation(Shape A, Shape B) {
		this.A = A;
		this.B = B;
	}
	
	// TODO: implement
	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException();
	}

}
