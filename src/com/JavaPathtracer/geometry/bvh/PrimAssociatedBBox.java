package com.JavaPathtracer.geometry.bvh;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Vector;

// Bounding box associated with a face index. Used during. BVH construction.
public class PrimAssociatedBBox extends BoundingBox {
	
	public int faceIndex;
	
	public PrimAssociatedBBox(Vector min, Vector max, int faceIndex) {
		super(min, max);
		this.faceIndex = faceIndex;
	}
	
}
