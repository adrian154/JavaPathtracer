package com.JavaPathtracer.geometry.octree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Mesh;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.bvh.BVHNode;
import com.JavaPathtracer.geometry.bvh.PrimAssociatedBBox;

public class OctreeMesh extends Mesh {

	public OctreeNode octree;
	
	public OctreeMesh(File file) throws IOException {
		super(file);
		this.buildOctree();
	}
	
	private void buildOctree() {
		
		List<PrimAssociatedBBox> children = new ArrayList<PrimAssociatedBBox>();
		for(int i = 0; i < this.faces.length / 3; i++) {
			Vector v0 = this.vertexes[this.faces[i * 3]];
			Vector v1 = this.vertexes[this.faces[i * 3 + 1]];
			Vector v2 = this.vertexes[this.faces[i * 3 + 2]];
			children.add(BVHNode.getBoxOfTri(i, v0, v1, v2));
		}
		
		BoundingBox box = BVHNode.getBoundingBoxOfBoxes(children);
		octree = new OctreeNode(this, box.min, box.max, children);
		octree.split(0, 5);
		
	}
	
	@Override
	public Hit intersect(Ray ray) {
		return octree.intersect(ray);
	}
	
}
