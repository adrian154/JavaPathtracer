package com.JavaPathtracer.geometry;

import com.JavaPathtracer.material.Material;

public class ObjectHit extends Hit {

	public final Material material;
	
	public ObjectHit(Hit hit, Material material) {
		super(hit.ray, hit.point, hit.normal, hit.tangent, hit.distance, hit.textureCoordinates);
		this.material = material;
	}
	
}
