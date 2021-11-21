package com.JavaPathtracer.geometry.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.scene.WorldObject;

public class BVHNode extends BoundingBox implements WorldObject {

	// construction constants
	public static final int NUM_BINS = 10; // more bins = potentially better splits but greater construction time
	public static final double COST_TRAVERSE = 1;
	public static final double COST_INTERSECT = 12;
	public static final int MAX_DEPTH = 12;
	
	public Mesh mesh;
	
	// children
	public BVHNode left;
	public BVHNode right;

	// list of primitives (for leaf nodes)
	private int[] triangleIndexes;

	// list of children, used during BVH construction (should be null afterwards)
	private List<TriangleBoundingBox> primitives;

	public static BVHNode create(Mesh mesh) {

		
		List<TriangleBoundingBox> tris = new ArrayList<TriangleBoundingBox>();
		for(int i = 0; i < mesh.faces.length / 3; i++) {
			tris.add(TriangleBoundingBox.create(mesh, i));
		}
		
		BVHNode root = new BVHNode(mesh, tris);
		root.split(0);
		return root;

	}

	public BVHNode(Mesh mesh, List<TriangleBoundingBox> primitives) {
		super(primitives);
		this.mesh = mesh;
		this.primitives = primitives;
	}
	
	private void makeLeafNode() {
		this.triangleIndexes = this.primitives.stream().mapToInt(child -> Integer.valueOf(child.face)).toArray();
		this.primitives = null;
		return;
	}

	public int numPrimitives() {
		return this.primitives.size();
	}
	
	public void split(int depth) {
		
		if (depth >= MAX_DEPTH) {
			this.makeLeafNode();
			return;
		}

		// Use the surface-area heuristic to determine the optimal split.
		// Place the primitives into a certain number of evenly spaced bins and evaluate each split.
		
		double bestSplitCost = Double.POSITIVE_INFINITY; 
		BVHNode bestSplitLeft = null, bestSplitRight = null;
		double noSplitCost = primitives.size() * COST_INTERSECT;

		// TODO: use some kind of heuristic to avoid checking splits along all three axes
		// TODO: figure out if binning primitives by the TRIANGLE centroid rather than the bounding box centroid results in better BVHs
		for (int axis = 0; axis < 3; axis++) {

			List<List<TriangleBoundingBox>> bins = new ArrayList<>();
			for(int i = 0; i < NUM_BINS; i++) {
				bins.add(new ArrayList<>());
			}

			// place primitives into bins based on centroid 
			for(TriangleBoundingBox box: this.primitives) {
				double centroid = box.centroid().get(axis);
				int bin = (int)Math.floor((centroid - this.min.get(axis)) / NUM_BINS);
				bins.get(bin).add(box);
			}
			
			// iterate over potential splits and figure out which one's the best
			for(int split = 1; split < NUM_BINS; split++) {

				BVHNode left = new BVHNode(mesh, bins.subList(0, split).stream().flatMap(List::stream).collect(Collectors.toList()));
				BVHNode right = new BVHNode(mesh, bins.subList(split, NUM_BINS).stream().flatMap(List::stream).collect(Collectors.toList()));
				
				double splitCost = COST_TRAVERSE +
					(left.surfaceArea() / this.surfaceArea() * left.numPrimitives() * COST_INTERSECT) +
					(right.surfaceArea() / this.surfaceArea() * right.numPrimitives() * COST_INTERSECT);

				if (splitCost < bestSplitCost) {
					bestSplitCost = splitCost;
					bestSplitLeft = left;
					bestSplitRight = right;
				}

			}

		}

		if (bestSplitCost < noSplitCost) {

			// only keep the best children, destroy all others
			this.left = bestSplitLeft;
			this.right = bestSplitRight;

			// recurse
			this.left.split(depth + 1);
			this.right.split(depth + 1);

			primitives = null;
			
		} else {
			this.makeLeafNode();
		}

	}

	@Override
	public ObjectHit traceRay(Ray ray) {
		
		// check if the ray intersects the bounding box
		if (!super.intersects(ray))
			return ObjectHit.MISS;

		// leaf node
		if (triangleIndexes == null) {

			ObjectHit left = this.left.traceRay(ray);
			ObjectHit right = this.right.traceRay(ray);
			if(!left.hit) return right;
			if(!right.hit) return left;
			
			return left.distance < right.distance ? left : right;
			
		} else {
			return mesh.intersect(ray, this, this.triangleIndexes);
		}

	}

}