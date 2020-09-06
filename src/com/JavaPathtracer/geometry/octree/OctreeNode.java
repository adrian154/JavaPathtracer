package com.JavaPathtracer.geometry.octree;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Main;
import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Mesh;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.bvh.PrimAssociatedBBox;

public class OctreeNode extends BoundingBox implements Shape {
	
	private Mesh mesh;
	public OctreeNode children[];
	public int containedTriangles[]; // present on leaf nodes, absent on others
	public List<PrimAssociatedBBox> prims; // used in construction
	
	public OctreeNode(Mesh mesh, Vector min, Vector max, List<PrimAssociatedBBox> prims) {
		super(min, max);
		this.mesh = mesh;
		this.prims = prims;
	}
	
	public void split(int depth, int maxDepth) {
		
		// Leaf node
		if(depth >= maxDepth) {
			containedTriangles = prims.stream().mapToInt(box -> Integer.valueOf(box.faceIndex)).toArray();
			prims = null;
			return;
		}
		
		// Populate children
		children = new OctreeNode[8];
		
		Vector center = min.plus(max).divBy(2);
		int index = 0;
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				for(int k = 0; k < 2; k++) {
					children[index] = new OctreeNode(
						this.mesh,
						new Vector(
							i == 1 ? center.x : min.x,
							j == 1 ? center.y : min.y,
							k == 1 ? center.z : min.z
						),
						new Vector(
							i == 1 ? max.x : center.x,
							j == 1 ? max.y : center.y,
							k == 1 ? max.z : center.z
						),
						null
					);
					index++;
				}
			}
		}
		
		// Assign primitives to children
		for(int i = 0; i < children.length; i++) {
			
			OctreeNode node = children[i];
			
			node.prims = new ArrayList<PrimAssociatedBBox>();
			for(PrimAssociatedBBox box: this.prims) {
				if(node.overlapsWith(box)) node.prims.add(box);
			}
			
			// Recurse
			if(node.prims.size() == 0) {
				children[i] = null;
			} else {
				System.out.println(Main.repeat("\t", depth) + "node " + node + " has " + node.prims.size());
				node.split(depth + 1, maxDepth);
			}
			
		}
		
		prims = null;
		
	}
	
	public Hit intersect(Ray ray) {
		
		if(!super.intersect(ray).hit) return Hit.MISS;

		if(this.children == null) {
			return this.mesh.intersect(ray, this.containedTriangles);
			//return this.intersectSlow(ray);
		}

		Hit nearest = Hit.MISS;
		for(OctreeNode node: children) {
			if(node != null) {
				Hit hit = node.intersect(ray);
				if(hit.hit && hit.distance < nearest.distance)
					nearest = hit;
			}
		}
		
		return nearest;

	}
	
}
