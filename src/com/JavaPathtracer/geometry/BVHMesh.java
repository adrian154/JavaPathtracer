package com.JavaPathtracer.geometry;

import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.bvh.BVHNode;

public class BVHMesh extends Mesh implements Shape {

	private BVHNode BVHRoot;
	
	public BVHMesh(File file) throws IOException {
		this(file, new Matrix());
	}
	
	public BVHMesh(File file, Matrix matrix) throws IOException {
		super(file, matrix);
		BVHRoot = new BVHNode(this);
	}

	public Hit intersect(Ray ray) {
		
		Hit hit = BVHRoot.intersect(ray);
		
		if(hit.hit)
			hit.normal.normalize(); // Normalize at the last minute instead of at every intersection
		
		return hit;
		
	}
	
	public Sphere getBoundingSphere() {
		return this.BVHRoot.toSphere();
	}
	
}