package com.JavaPathtracer.geometry.mesh;

import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.geometry.Vector;

public class BVHNode extends BoundingBox implements Shape {

	public MeshGeometryContainer mesh;
	public BVHNode left;
	public BVHNode right;
	public int[] primIndexes;
	public List<PrimAssociatedBBox> children; // USED DURING CONSTRUCTION. NULL AFTER BVH DONE BUILDING SO IT CAN BE

	// More bins = higher quality BVH at the cost of slower construction
	public static final int NUM_BINS = 32;
	public static final double COST_TRAVERSE = 1; // greater intersect cost = more splits
	public static final double COST_INTERSECT = 12;
	public static final int MAX_DEPTH = 12;

	private static final double minOf3(double a, double b, double c) {
		return Math.min(a, Math.min(b, c));
	}

	private static final double maxOf3(double a, double b, double c) {
		return Math.max(a, Math.max(b, c));
	}

	public BVHNode(MeshGeometryContainer mesh) {

		// Appease the compiler...
		super(null, null);

		this.mesh = mesh;
		this.children = new ArrayList<PrimAssociatedBBox>();

		for (int face = 0; face < mesh.faces.length / 3; face++) {

			Vector v0 = mesh.vertexes[mesh.faces[face * 3]];
			Vector v1 = mesh.vertexes[mesh.faces[face * 3 + 1]];
			Vector v2 = mesh.vertexes[mesh.faces[face * 3 + 2]];

			children.add(getBoxOfTri(face, v0, v1, v2));

		}

		BoundingBox self = getBoundingBoxOfBoxes(this.children);
		this.min = self.min;
		this.max = self.max;

		// Ready to build, finally!
		split(0);

	}

	public BVHNode(MeshGeometryContainer mesh, BoundingBox box, List<PrimAssociatedBBox> boxes) {
		super(box.min, box.max);
		this.mesh = mesh;
		this.children = boxes;
	}

	public static BoundingBox getBoundingBoxOfBoxes(List<? extends BoundingBox> boxes) {

		Vector min = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		Vector max = new Vector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);

		for (BoundingBox box : boxes) {
			min.x = Math.min(min.x, box.min.x);
			min.y = Math.min(min.y, box.min.y);
			min.z = Math.min(min.z, box.min.z);

			max.x = Math.max(max.x, box.max.x);
			max.y = Math.max(max.y, box.max.y);
			max.z = Math.max(max.z, box.max.z);
		}

		BoundingBox result = new BoundingBox(min, max);
		return result;

	}

	public static PrimAssociatedBBox getBoxOfTri(int face, Vector v0, Vector v1, Vector v2) {

		Vector min = new Vector();
		Vector max = new Vector();

		min.x = minOf3(v0.x, v1.x, v2.x);
		min.y = minOf3(v0.y, v1.y, v2.y);
		min.z = minOf3(v0.z, v1.z, v2.z);

		max.x = maxOf3(v0.x, v1.x, v2.x);
		max.y = maxOf3(v0.y, v1.y, v2.y);
		max.z = maxOf3(v0.z, v1.z, v2.z);

		return new PrimAssociatedBBox(min, max, face);

	}

	public void split(int depth) {

		if (depth >= MAX_DEPTH) {
			primIndexes = children.stream().mapToInt(child -> Integer.valueOf(child.faceIndex)).toArray();
			children = null;
			return;
		}

		// Find optimal split
		double minSplitCost = Double.POSITIVE_INFINITY;
		BoundingBox minSplitLeftBox = null;
		BoundingBox minSplitRightBox = null;
		List<PrimAssociatedBBox> minSplitLeftChildren = null;
		List<PrimAssociatedBBox> minSplitRightChildren = null;

		double noSplitCost = children.size() * COST_INTERSECT;

		for (int axis = 0; axis < 3; axis++) {

			for (int split = 1; split < NUM_BINS - 1; split++) {

				double splitPos = this.min.get(axis)
						+ ((double) split / NUM_BINS) * (this.max.get(axis) - this.min.get(axis));

				List<PrimAssociatedBBox> left = new ArrayList<PrimAssociatedBBox>();
				List<PrimAssociatedBBox> right = new ArrayList<PrimAssociatedBBox>();

				for (PrimAssociatedBBox box : this.children) {

					double centroid = (box.min.get(axis) + box.max.get(axis)) / 2;
					if (centroid < splitPos) {
						left.add(box);
					} else {
						right.add(box);
					}

				}

				BoundingBox leftBox = getBoundingBoxOfBoxes(left);
				BoundingBox rightBox = getBoundingBoxOfBoxes(right);

				double splitCost = COST_TRAVERSE + (leftBox.area() / this.area() * left.size() * COST_INTERSECT)
						+ (rightBox.area() / this.area() * right.size() * COST_INTERSECT);

				if (splitCost < minSplitCost) {
					minSplitCost = splitCost;
					minSplitLeftBox = leftBox;
					minSplitRightBox = rightBox;
					minSplitLeftChildren = left;
					minSplitRightChildren = right;
				}

			}

		}

		if (minSplitCost < noSplitCost) {

			// Split!
			this.left = new BVHNode(this.mesh, minSplitLeftBox, minSplitLeftChildren);
			this.right = new BVHNode(this.mesh, minSplitRightBox, minSplitRightChildren);

			// Recurse
			this.left.split(depth + 1);
			this.right.split(depth + 1);

		} else {

			// Attach children
			primIndexes = children.stream().mapToInt(child -> Integer.valueOf(child.faceIndex)).toArray();

		}

		// In any case, children array is no longer needed
		children = null;

	}

	@Override
	public Hit intersect(Ray ray) {

		// intersect self, first
		Hit self = super.intersectFast(ray);
		if (!self.hit)
			return self;

		if (left == null && right == null) {
			if (this.primIndexes != null) {
				return mesh.intersect(ray, this.primIndexes);
			} else {
				return Hit.MISS;
			}
		} else {

			Hit hl, hr;
			hl = this.left.intersect(ray);
			hr = this.right.intersect(ray);
			return hl.distance < hr.distance ? hl : hr;

		}

	}

}