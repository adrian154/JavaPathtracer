package com.JavaPathtracer.geometry;

import java.io.File;
import java.io.IOException;

// For testing ONLY!
// When I say "slow" I REALLY MEAN IT!
public class SlowMesh extends Mesh implements Shape {
	
	public SlowMesh(File file) throws IOException {
		super(file);
	}
	
	public Hit intersect(Ray ray) {
		return super.intersect(ray);
	}
	
}
