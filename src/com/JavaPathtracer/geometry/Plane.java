package com.JavaPathtracer.geometry;

import com.JavaPathtracer.Pathtracer;

public class Plane implements Shape {

	public Vector normal;
	public Vector point;
	public Vector tangent;
	public double tilingSize;
	public boolean oneSided;

	public Plane(Vector normal, Vector point) {
		this(normal, point, 1.0);
	}
	
	public Plane(Vector normal, Vector point, double tilingSize) {
		this(normal, point, tilingSize, false, normal.getOrthagonal());
	}
	
	public Plane(Vector normal, Vector point, Vector tangent) {
		this(normal, point, 1.0, false, tangent);
	}

	public Plane(Vector normal, Vector point, double tilingSize, boolean oneSided, Vector tangent) {
		this.normal = normal.normalize();
		this.point = point;
		this.tilingSize = tilingSize;
		this.oneSided = oneSided;
		this.tangent = tangent;
	}

	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException("You must be insane.");
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		// reject rays that are wonky (wrong side!))
		if(oneSided && ray.direction.dot(this.normal) > 0) return Hit.MISS;
		
		/* Check if equation has solution. */
		double denom = ray.direction.dot(this.normal);
		if (denom == 0) {
			return Hit.MISS;
		}

		/* Solve for the distance. */
		double distance = this.point.minus(ray.origin).dot(this.normal) / denom;

		/* Make sure hit point is not behind ray. */
		if (distance < Pathtracer.EPSILON) {
			return Hit.MISS;
		}

		Vector point = ray.getPoint(distance);
		Vector vec = point.minus(this.point);
		Vector ortho = normal.getOrthagonal();
		double u = ortho.dot(vec);
		double v = normal.cross(ortho).dot(vec);
		u = u - Math.floor(u / this.tilingSize) * this.tilingSize;
		v = v - Math.floor(v / this.tilingSize) * this.tilingSize;
	
		return new Hit(ray, point, normal, tangent, distance, new Vector(u / this.tilingSize, v / this.tilingSize, 0.0));

	}

}
