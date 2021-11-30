package com.JavaPathtracer.geometry.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.JavaPathtracer.Stopwatch;
import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.scene.WorldObject;

public class BVHNode extends BoundingBox implements Shape {

	// construction constants
	public static final int NUM_BINS = 16; // more bins = potentially better splits but greater construction time
	public static final double COST_TRAVERSE = 1;
	public static final double COST_INTERSECT = 12;
	public static final int MAX_DEPTH = 12;
		
	// tree node fields
	public BVHNode left;
	public BVHNode right;
	private List<WorldObject> objects;

	// some stuff used during construction
	private List<Primitive> primitives;
	
	// for tracking progress
	private BVHNode root;
	private int numProcessedPrimitives;
	
	public BVHNode(List<? extends WorldObject> objects) {
		
		super(null, null);
		
		Stopwatch.start("building BVH");
		
		// create list of primitives
		this.primitives = new ArrayList<Primitive>();
		for(WorldObject object: objects) {
			this.primitives.add(new Primitive(object));
		}
		
		BoundingBox box = new BoundingBox(primitives);
		this.min = box.min;
		this.max = box.max;
		this.numProcessedPrimitives = 0;
		this.split(0, this);
		
		Stopwatch.finish();
		
	}
	
	// the root BVH node is passed partially so child nodes can track progress and partially to distinguish these two type constructors
	// ...which would otherwise conflict due to type erasure
	public BVHNode(BVHNode root, List<Primitive> primitives) {
		super(primitives);
		this.primitives = primitives;
		this.root = root;
	}
	
	private void makeLeafNode() {
		
		// pluck objects from list of primitives
		this.objects = this.primitives.stream().map(child -> child.object).collect(Collectors.toList());
		
		// debug logging
		root.numProcessedPrimitives += this.primitives.size();
		System.out.printf("Processed %d / %d primitives (%d%%)\n", root.numProcessedPrimitives, root.primitives.size(), root.numProcessedPrimitives * 100 / root.primitives.size());
		
		// let the GC clean up the primitives - we won't need them again
		this.primitives = null;
		
	}

	public int numPrimitives() {
		return this.primitives.size();
	}

	// root is passed to track progress
	public void split(int depth, BVHNode root) {
		
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
		// TODO: figure out if binning primitives by the triangle centroid rather than the bounding box centroid results in better BVHs
		for (int axis = 0; axis < 3; axis++) {

			List<List<Primitive>> bins = new ArrayList<>();
			for(int i = 0; i < NUM_BINS; i++) {
				bins.add(new ArrayList<>());
			}

			// place primitives into bins based on centroid
			double binWidth = (this.max.get(axis) - this.min.get(axis)) / NUM_BINS;
			for(Primitive box: this.primitives) {
				double centroid = box.centroid().get(axis);
				int bin = (int)Math.min(Math.floor((centroid - this.min.get(axis)) / binWidth), NUM_BINS - 1);
				bins.get(bin).add(box);
			}
			
			// iterate over potential splits and figure out which one's the best
			for(int split = 1; split < NUM_BINS; split++) {

				BVHNode left = new BVHNode(root, bins.subList(0, split).stream().flatMap(List::stream).collect(Collectors.toList()));
				BVHNode right = new BVHNode(root, bins.subList(split, NUM_BINS).stream().flatMap(List::stream).collect(Collectors.toList()));
		
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
			this.makeLeafNode();
		}

	}

	@Override
	public ObjectHit raytrace(Ray ray) {
		
		// check if the ray intersects the bounding box
		if (!this.intersects(ray))
			return ObjectHit.MISS;
		
		// not a leaf node
		if (objects == null) {

			ObjectHit left = this.left.raytrace(ray);
			ObjectHit right = this.right.raytrace(ray);
			if(!left.hit) return right;
			if(!right.hit) return left;
			
			return left.distance < right.distance ? left : right;
			
		} else {
			
			// find nearest intersecting object
			Hit nearest = Hit.MISS;
			for(WorldObject object: objects) {
				
			}
			return mesh.intersect(ray, this.triangleIndexes);
		
		}

	}

}