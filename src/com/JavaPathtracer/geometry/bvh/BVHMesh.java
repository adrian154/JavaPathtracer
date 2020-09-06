package com.JavaPathtracer.geometry.bvh;

import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Mesh;
import com.JavaPathtracer.geometry.Ray;

public class BVHMesh extends Mesh {

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