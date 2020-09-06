package com.JavaPathtracer.geometry;

import java.io.File;
import java.io.IOException;

public class BVHMesh extends Mesh implements Shape {

	private BVHNode BVHRoot;
	
	public BVHMesh(File file) throws IOException {
		super(file);
		BVHRoot = new BVHNode(this);
	}

	public Hit intersect(Ray ray) {
		
		// Delicious recursion!
		return BVHRoot.intersect(ray);
		
	}
	
}