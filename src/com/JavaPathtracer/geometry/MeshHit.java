package com.JavaPathtracer.geometry;

public class MeshHit extends Hit {

	public int face;
	
	public MeshHit(Vector point, Vector normal, double distance, Vector textureCoordinates, int face) {
		super(true, point, normal, distance, textureCoordinates);
		this.face = face;
	}
	
}
