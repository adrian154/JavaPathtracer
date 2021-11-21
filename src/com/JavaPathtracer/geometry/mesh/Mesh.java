package com.JavaPathtracer.geometry.mesh;

import java.util.Map;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Transform;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.WorldObject;

public class Mesh extends WorldObject {

	private BVHNode bvh;
	private MeshGeometry mesh;
	private Map<String, Material> materials;
	
	public Mesh(MeshGeometry mesh, Transform transform, Map<String, Material> materials) {
		super(transform);
		this.mesh = mesh;
		this.bvh = new BVHNode(mesh);
		this.materials = materials;
	}
	
	public Mesh(MeshGeometry mesh, Map<String, Material> materials) {
		this(mesh, null, materials);
	}
	
	@Override
	public ObjectHit raytraceObject(Ray ray) {
		
		Hit hit = bvh.raytrace(ray);
		
		if(hit.hit) {
			MeshHit meshHit = (MeshHit)hit;
			String materialID = mesh.materialIDs[meshHit.face];
			return new ObjectHit(hit, this, materials.containsKey(materialID) ? materials.get(materialID) : materials.get("default"));
		}
		
		return ObjectHit.MISS;
		
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return this.bvh;
	}
	
}
