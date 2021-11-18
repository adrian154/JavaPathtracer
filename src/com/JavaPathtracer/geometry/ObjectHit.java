package com.JavaPathtracer.geometry;

import com.JavaPathtracer.material.Material;

public class ObjectHit {

	public static final ObjectHit MISS = new ObjectHit(Hit.MISS, null);
	
	public final Hit hit;
	public final Material material;
	
	public ObjectHit(Hit hit, Material material) {
		this.hit = hit;
		this.material = material;
	}
	
}
