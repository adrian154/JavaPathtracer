package com.JavaPathtracer.geometry.mesh;

import java.util.Map;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Transform;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.WorldObject;

public class Mesh extends WorldObject {

	private BVHNode bvh;
	private Map<String, Material> materials;
	
	public Mesh(BVHNode bvh, Transform transform, Map<String, Material> materials) {
		super(transform);
		this.bvh = bvh;
		this.materials = materials;
	}
	
	public Mesh(BVHNode bvh, Map<String, Material> materials) {
		this(bvh, null, materials);
	}
	
	@Override
	public ObjectHit raytraceLocal(Ray ray) {
		
		Hit hit = bvh.raytrace(ray);
		
		if(hit.hit) {
			MeshHit meshHit = (MeshHit)hit;
			return new ObjectHit(hit, this, materials.containsKey(meshHit.materialID) ? materials.get(meshHit.materialID) : materials.get(""));
		}
		
		return ObjectHit.MISS;
		
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return this.bvh;
	}
	
	@Override
	public Vector pickRandomPoint() {
		throw new UnsupportedOperationException();
	}
	
}
