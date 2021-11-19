package com.JavaPathtracer.geometry;

import com.JavaPathtracer.material.Material;

public class ObjectHit extends Hit {

	public static final ObjectHit MISS = new ObjectHit(Hit.MISS, null);
	
	public final Material material;
	
	public ObjectHit(Hit hit, Material material) {
		super(hit);
		this.material = material;
	}
	
}
