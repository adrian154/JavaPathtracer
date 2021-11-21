package com.JavaPathtracer.geometry.mesh;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class MeshHit extends Hit {

	public final int face;
	
	public MeshHit(Ray ray, Vector point, Vector normal, Vector tangent, double distance, Vector textureCoordinate, int face) {
		super(ray, point, normal, tangent, distance, textureCoordinate);
		this.face = face;
	}
	
}
