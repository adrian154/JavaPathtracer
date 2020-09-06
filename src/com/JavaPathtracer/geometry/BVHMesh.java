package com.JavaPathtracer.geometry;

import java.io.File;
import java.io.IOException;

public class BVHMesh extends Mesh implements Shape {

	private BVHNode BVHRoot;
	
	public BVHMesh(File file) throws IOException {
		super(file);
		BVHRoot = new BVHNode(this);
	
		// debug the bvh
		debugBVH(BVHRoot, 0);
	}
	
	public String repeat(String s, int times) {
		String result = "";
		for(int i = 0; i < times; i++){
			result += s;
		}
		return result;
	}
	
	public void debugBVH(BVHNode node, int depth) {
		System.out.printf("%snode is from (%f,%f,%f) to (%f,%f,%f)\n", repeat("\t", depth), node.min.x, node.min.y, node.min.z, node.max.x, node.max.y, node.max.z);
		if(node.left != null) {
			debugBVH(node.left, depth + 1);
			debugBVH(node.right, depth + 1);
		}
	}
	
	public Hit intersect(Ray ray) {
		
		// Delicious recursion!
		return BVHRoot.intersect(ray);
		
	}
	
}