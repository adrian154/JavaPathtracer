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
	public static final int NUM_BINS = 16; // more bins = potentially better splits but greater construction time
	public static final double COST_TRAVERSE = 1;
	public static final double COST_INTERSECT = 8; // PBRT's estimate seems fair
	public static final int MAX_DEPTH = 8;
	
	public MeshGeometry mesh;
	
	// children
	public BVHNode left;
	public BVHNode right;

	// list of primitives (for leaf nodes)
	private int[] triangleIndexes;

	// some stuff used during construction
	private List<TriangleBoundingBox> primitives;
	private int numProcessedPrimitives;
	
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
		this.numProcessedPrimitives = 0;
		this.split(0, this);
		System.out.println(this);
		
	}
	
	public BVHNode(MeshGeometry mesh, List<TriangleBoundingBox> primitives) {
		super(primitives);
		this.primitives = primitives;
		this.mesh = mesh;
	}
	
	private void makeLeafNode(BVHNode root) {
		this.triangleIndexes = this.primitives.stream().mapToInt(child -> Integer.valueOf(child.face)).toArray();
		root.numProcessedPrimitives += this.primitives.size();
		System.out.printf("Processed %d / %d primitives (%d%%)\n", root.numProcessedPrimitives, root.primitives.size(), root.numProcessedPrimitives * 100 / root.primitives.size());
		this.primitives = null;
		return;
	}

	public int numPrimitives() {
		return this.primitives.size();
	}

	// root is passed to track progress
	public void split(int depth, BVHNode root) {
		
		if (depth >= MAX_DEPTH) {
			this.makeLeafNode(root);
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
				int bin = (int)Math.min(Math.floor((centroid - this.min.get(axis)) / binWidth), NUM_BINS - 1);
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
			this.left.split(depth + 1, root);
			this.right.split(depth + 1, root);

			primitives = null;
			
		} else {
			this.makeLeafNode(root);
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
			return mesh.intersect(ray, this.triangleIndexes);
		}

	}

}