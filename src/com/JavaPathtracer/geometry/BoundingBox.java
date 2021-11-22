package com.JavaPathtracer.geometry;

import java.util.List;

import com.JavaPathtracer.Pathtracer;

// Axis-aligned bounding box
public class BoundingBox implements Shape {

	public Vector min;
	public Vector max;

	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}
	
	public BoundingBox(BoundingBox box) {
		this(box.min, box.max);
	}

	// see BoundingBox#bound
	public BoundingBox(List<? extends Shape> shapes) {
		this(BoundingBox.bound(shapes));
	}
	
	public static final Vector min(Vector A, Vector B) {
		return new Vector(
			Math.min(A.x, B.x),
			Math.min(A.y, B.y),
			Math.min(A.z, B.z)
		);
	}
	
	public static final Vector max(Vector A, Vector B) {
		return new Vector(
			Math.max(A.x, B.x),
			Math.max(A.y, B.y),
			Math.max(A.z, B.z)
		);
	}
	
	// static method because Java doesn't support *any* code before a constructor call
	private static BoundingBox bound(List<? extends Shape> shapes) {
		
		Vector min = new Vector();
		Vector max = new Vector();
		
		for(Shape shape: shapes) {
			
			BoundingBox box = shape.getBoundingBox();
			min = BoundingBox.min(box.min, min);
			max = BoundingBox.max(box.max, max);
			
		}
		
		return new BoundingBox(min, max);
		
	}
	
	public double surfaceArea() {
		double width = this.width(), height = this.height(), depth = this.depth();
		return 2 * (width * height + width * depth + height * depth);
	}

	public double width() {
		return max.x - min.x;
	}

	public double height() {
		return max.y - min.y;
	}

	public double depth() {
		return max.z - min.z;
	}

	public double volume() {
		return this.width() * this.height() * this.depth();
	}

	public Vector centroid() {
		return min.plus(max).divBy(2);
	}

	// intersect by calculating intersection with 6 planes and finding the nearest
	public boolean intersects(Ray ray) {

		// micro-optimization: avoid repeated divisions by  predividing constants
		double invX = 1 / ray.direction.x;
		double invY = 1 / ray.direction.y;
		double invZ = 1 / ray.direction.z;
		
		// x-planes
		double t1 = (min.x - ray.origin.x) * invX;
		double t2 = (max.x - ray.origin.x) * invX;
		
		// y-planes
		double t3 = (min.y - ray.origin.y) * invY;
		double t4 = (max.y - ray.origin.y) * invY;
		
		// z-planes
		double t5 = (min.z - ray.origin.z) * invZ;
		double t6 = (max.z - ray.origin.z) * invZ;
		
		// eliminate points which can't intersect the bounding box
		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

		// no intersection if the point is behind the origin or the two intersection points are not properly ordered
		if (tmax < Pathtracer.EPSILON) {
			return false;
		}
		
		if(tmin > tmax) {
			return false;
		}

		return true;
	
	}
	
	@Override
	public Hit raytrace(Ray ray) {
		
		//throw new UnsupportedOperationException();
		
		double invX = 1 / ray.direction.x;
		double invY = 1 / ray.direction.y;
		double invZ = 1 / ray.direction.z;
		
		// x-planes
		double t1 = (min.x - ray.origin.x) * invX;
		double t2 = (max.x - ray.origin.x) * invX;
		double t3 = (min.y - ray.origin.y) * invY;
		double t4 = (max.y - ray.origin.y) * invY;
		double t5 = (min.z - ray.origin.z) * invZ;
		double t6 = (max.z - ray.origin.z) * invZ;
		
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
		
		// the actual intersect point at `tmin` units along the ray
		// which face is intersected will depend on which value of t1..t6 is tmin.
		double x = Math.min(t1, t2), y = Math.min(t3, t4), z = Math.min(t5, t6);
		
		if(tmax < Pathtracer.EPSILON || x > tmax || y > tmax || z > tmax) {
			return Hit.MISS;
		}
		
		double t;
		Vector normal;

		if(x > y && x > z) {
			t = x;
			normal = t1 < t2 ? Vector.NEGATIVE_X : Vector.X;
		} else if(y > z) {
			t = y;
			normal = t3 < t4 ? Vector.NEGATIVE_Y : Vector.Y;
		} else {
			t = z;
			normal = t5 < t6 ? Vector.NEGATIVE_Z : Vector.Z;
		}
		
		// dummy tangent and texture coordinate
		return new Hit(ray, ray.getPoint(t), normal, null, t, Vector.ZERO);
		
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		return this;
	}

	public boolean contains(BoundingBox other) {
		return this.contains(other.min) && this.contains(other.max);
	}

	public boolean contains(Vector point) {
		return point.x > this.min.x && point.y > this.min.y && point.z > this.min.z &&
			   point.x < this.max.x && point.y < this.max.y && point.z < this.max.z;
	}

	public Sphere toSphere() {
		Vector center = this.centroid();
		return new Sphere(center, this.max.minus(center).length());
	}

	public static final boolean overlaps(BoundingBox a, BoundingBox b) {
		return (a.min.x <= b.min.x && b.min.x <= a.max.x) || (a.min.x <= b.max.x && b.max.x <= a.max.x) ||
			   (b.min.x <= a.min.x && a.min.x <= b.max.x) || (b.min.x <= a.max.x && a.max.x <= b.max.x);
	}

	@Override
	public String toString() {
		return "(min=" + this.min + ", max=" + this.max + ")";
	}

}