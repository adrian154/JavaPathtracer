package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Raytracer;

// container class for geometry
public class BoundingBox implements FiniteShape {

	public final Vector min;
	public final Vector max;

	public BoundingBox(Vector min, Vector max) {
		this.min = min;
		this.max = max;
	}

	public double area() {
		double width = max.x - min.x;
		double height = max.y - min.y;
		double depth = max.z - min.z;
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
		return (max.x - min.x) * (max.y - min.y) * (max.z - min.z);
	}

	public Vector centroid() {
		return min.plus(max).divBy(2);
	}

	// intersect by basically calculating 
	public boolean intersect(Ray ray) {

		// micro-optimization: avoid repeated divisions by  predividing constants
		double invX = 1 / ray.direction.x;
		double invY = 1 / ray.direction.y;
		double invZ = 1 / ray.direction.z;
		double t1 = (min.x - ray.origin.x) * invX;
		double t2 = (max.x - ray.origin.x) * invX;
		double t3 = (min.y - ray.origin.y) * invY;
		double t4 = (max.y - ray.origin.y) * invY;
		double t5 = (min.z - ray.origin.z) * invZ;
		double t6 = (max.z - ray.origin.z) * invZ;
		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

		if (tmax < Pathtracer.EPSILON)
			return false;

		if (tmin > tmax)
			return false;

		return true;
		
	}

	@Override
	public Hit raytrace(Ray ray) {

		// micro-optimization: avoid repeated divisions by  predividing constants
		double invX = 1 / ray.direction.x;
		double invY = 1 / ray.direction.y;
		double invZ = 1 / ray.direction.z;
		double t1 = (min.x - ray.origin.x) * invX;
		double t2 = (max.x - ray.origin.x) * invX;
		double t3 = (min.y - ray.origin.y) * invY;
		double t4 = (max.y - ray.origin.y) * invY;
		double t5 = (min.z - ray.origin.z) * invZ;
		double t6 = (max.z - ray.origin.z) * invZ;
		double tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
		double tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

		/* Negative: AABB is behind the ray. */
		if (tmax < Pathtracer.EPSILON) {
			return null;
		}

		/* Minimum distance greater than maximum: No intersection. */
		if (tmin > tmax) {
			return null;
		}

		/* Ray intersects with the box. */
		double t = tmin;
		Vector point = ray.getPoint(t);

		Vector middle = min.plus(max.minus(min).divBy(2.0));
		double deltaX = point.x - middle.x;
		double deltaY = point.y - middle.y;
		double deltaZ = point.z - middle.z;

		Vector normal;
		if (Math.abs(Math.abs(deltaX) - this.width() / 2) < Pathtracer.EPSILON) {
			normal = new Vector(Math.signum(deltaX), 0.0, 0.0);
		} else if (Math.abs(Math.abs(deltaY) - this.height() / 2) < Pathtracer.EPSILON) {
			normal = new Vector(0.0, Math.signum(deltaY), 0.0);
		} else {
			normal = new Vector(0.0, 0.0, Math.signum(deltaZ));
		}

		// TODO:  calculate tangent vector
		return new Hit(ray, point, normal, null, t, new Vector(0.0, 0.0, 0.0));

	}
	
	@Override
	public Sphere getBoundingSphere() {
		return new Sphere(this.centroid(), this.max.minus(this.centroid()).length());
	}

	public boolean containsBox(BoundingBox other) {
		return other.min.x > this.min.x && other.min.y > this.min.y && other.min.z > this.min.z
				&& other.max.x < this.max.x && other.max.y < this.max.y && other.max.z < this.max.z;
	}

	public boolean containsPoint(Vector point) {
		return point.x > this.min.x && point.y > this.min.y && point.z > this.min.z && point.x < this.max.x
				&& point.y < this.max.y && point.z < this.max.z;
	}

	public Sphere toSphere() {
		Vector center = this.centroid();
		return new Sphere(center, this.max.minus(center).length());
	}

	public static final boolean overlap(BoundingBox a, BoundingBox b) {
		return (a.min.x <= b.min.x && b.min.x <= a.max.x) || (a.min.x <= b.max.x && b.max.x <= a.max.x)
				|| (b.min.x <= a.min.x && a.min.x <= b.max.x) || (b.min.x <= a.max.x && a.max.x <= b.max.x);
	}

	public void pad(double amount) {
		this.min.iadd(new Vector(-amount, -amount, -amount));
		this.max.iadd(new Vector(amount, amount, amount));
	}

	// For debugging purposes
	@Override
	public String toString() {
		return "(min=" + this.min + ", max=" + this.max + ")";
	}

}