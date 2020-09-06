package com.JavaPathtracer.geometry;

import java.io.File;
import java.io.IOException;

public class BVHMesh extends Mesh {

	private BVHNode BVHRoot;
	
	public BVHMesh(File file) throws IOException {
		super(file);
		BVHRoot = new BVHNode(this);
	}
	
}
