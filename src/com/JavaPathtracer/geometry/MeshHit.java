package com.JavaPathtracer.geometry;

public class MeshHit extends Hit {

	public int face;
	
	public MeshHit(Ray ray, Vector point, Vector normal, double distance, Vector textureCoordinates, int face) {
		super(ray, point, normal, distance, textureCoordinates);
		this.face = face;
	}
	
}
