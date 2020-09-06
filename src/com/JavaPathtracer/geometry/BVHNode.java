package com.JavaPathtracer.geometry;

public class BVHNode extends BoundingBox {

	public BVHNode left;
	public BVHNode right;
	public int[] primIndexes;
	
	public BVHNode(Vector min, Vector max) {
		super(min, max);
	}
	
}
