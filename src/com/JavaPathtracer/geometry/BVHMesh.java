package com.JavaPathtracer.geometry;

import java.io.File;
import java.io.IOException;

public class BVHMesh extends Mesh implements Shape {

	private BVHNode BVHRoot;
	
	public BVHMesh(File file) throws IOException {
		super(file);
		BVHRoot = new BVHNode(this);
	
		// debug the bvh
		debugBVH(BVHRoot);
	}
	
	public void debugBVH(BVHNode node) {
		System.out.printf("Node is from (%f,%f,%f) to (%f,%f,%f)\n", node.min.x, node.min.y, node.min.z, node.max.x, node.max.y, node.max.z);
		if(node.left != null) {
			debugBVH(node.left);
			debugBVH(node.right);
		}
	}
	
	public Hit intersect(Ray ray) {
		
		// Delicious recursion!
		return BVHRoot.intersect(ray);
		
	}
	
}