package com.JavaPathtracer.geometry;

import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.WorldObject;

public class ObjectHit extends Hit {

	public static final ObjectHit MISS = new ObjectHit(Hit.MISS, null, null);
	
	public final WorldObject object;
	public final Material material;
	
	public ObjectHit(Hit hit, WorldObject object, Material material) {
		super(hit);
		this.object = object;
		this.material = material;
	}
	
}
