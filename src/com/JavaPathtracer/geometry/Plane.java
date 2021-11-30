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
	
	public Plane(Vector normal, Vector point, boolean oneSided) {
		this(normal, point, 1.0, oneSided, normal.getOrthagonal());
	}

	public Plane(Vector normal, Vector point, double tilingSize, boolean oneSided, Vector tangent) {
		this.normal = normal.normalize();
		this.point = point;
		this.tilingSize = tilingSize;
		this.oneSided = oneSided;
		this.tangent = tangent;
	}

	@Override
	public Vector pickRandomPoint() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public BoundingBox getBoundingBox() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Hit raytrace(Ray ray) {

		// reject rays that are wonky (wrong side!))
		double dot = ray.direction.dot(this.normal);
		if(oneSided && dot > 0) return Hit.MISS;
		
		/* Check if equation has solution. */
		if (dot == 0) {
			return Hit.MISS;
		}

		/* Solve for the distance. */
		double distance = this.point.minus(ray.origin).dot(this.normal) / dot;

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
	
		return new Hit(ray, point, normal.facing(ray.direction), tangent, distance, new Vector(u / this.tilingSize, v / this.tilingSize, 0.0));

	}

}
