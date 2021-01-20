package com.JavaPathtracer.geometry.bvh;

import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Matrix;
import com.JavaPathtracer.geometry.Mesh;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;

public class BVHMesh extends Mesh implements FiniteShape {

	private BVHNode BVHRoot;

	public BVHMesh(File file) throws IOException {
		this(file, new Matrix());
	}

	public BVHMesh(File file, Matrix matrix) throws IOException {
		super(file, matrix);
		BVHRoot = new BVHNode(this);
		System.out.println("Box: " + BVHRoot);
	}

	@Override
	public Hit intersect(Ray ray) {

		Hit hit = BVHRoot.intersect(ray);

		if (hit.hit)
			hit.normal.normalize(); // Normalize at the last minute instead of at every intersection

		return hit;

	}

	public Sphere getBoundingSphere() {
		return this.BVHRoot.toSphere();
	}

}