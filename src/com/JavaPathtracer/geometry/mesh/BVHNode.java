package com.JavaPathtracer.geometry.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;

public class BVHNode extends BoundingBox implements Shape {

	// construction constants
	public static final int NUM_BINS = 12; // more bins = potentially better splits but greater construction time
	public static final double COST_TRAVERSE = 1;
	public static final double COST_INTERSECT = 8;
	public static final int MAX_DEPTH = 12;
	
	public MeshGeometry mesh;
	
	// children
	public BVHNode left;
	public BVHNode right;

	// list of primitives (for leaf nodes)
	private int[] triangleIndexes;

	// list of children, used during BVH construction (should be null afterwards)
	private List<TriangleBoundingBox> primitives;
	
	public BVHNode(MeshGeometry mesh) {
		
		super(null, null);
		this.mesh = mesh;
		
		// create list of primitives
		this.primitives = new ArrayList<TriangleBoundingBox>();
		for(int i = 0; i < mesh.faces.length / 3; i++) {
			this.primitives.add(TriangleBoundingBox.create(mesh, i));
		}
		
		BoundingBox box = new BoundingBox(primitives);
		this.min = box.min;
		this.max = box.max;
		this.split(0);
		
	}
	
	public BVHNode(MeshGeometry mesh, List<TriangleBoundingBox> primitives) {
		super(primitives);
		this.primitives = primitives;
		this.mesh = mesh;
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
			double binWidth = (this.max.get(axis) - this.min.get(axis)) / NUM_BINS;
			for(TriangleBoundingBox box: this.primitives) {
				double centroid = box.centroid().get(axis);
				int bin = (int)Math.floor((centroid - this.min.get(axis)) / binWidth);
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
	public Hit raytrace(Ray ray) {
		
		// check if the ray intersects the bounding box
		if (!this.intersects(ray))
			return Hit.MISS;
		
		// leaf node
		if (triangleIndexes == null) {

			Hit left = this.left.raytrace(ray);
			Hit right = this.right.raytrace(ray);
			if(!left.hit) return right;
			if(!right.hit) return left;
			
			return left.distance < right.distance ? left : right;
			
		} else {
			//return super.raytrace(ray);
			return mesh.intersect(ray, this.triangleIndexes);
		}

	}

}