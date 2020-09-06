package com.JavaPathtracer.geometry;

import com.JavaPathtracer.material.Sampleable;

// 3D vector class
// Implements Sampleable so you can use Vectors as colors, kind of janky but oh well
public class Vector implements Sampleable {
	
	// Components
	public double x, y, z;
	
	public Vector() {
		
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	// This could be useful, I guess
	public double get(int component) {
		return component == 0 ? x : (component == 1 ? y : z);
	}
	
	// MUTABLE ADD
	// Use for accumulating color only
	public void add(Vector other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
	}
	
	// MUTABLE INVERT
	// In case, you know...
	public void invert() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}
	
	public Vector plus(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
	}
	
	public Vector minus(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	// Direct multiplication, should only be used on colors
	public Vector times(Vector other) {
		return new Vector(this.x * other.x, this.y * other.y, this.z * other.z);
	}
	
	// Scalar multiplication
	public Vector times(double scalar) {
		return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
	}
	
	// Scalar division
	public Vector divBy(double scalar) {
		return new Vector(this.x / scalar, this.y / scalar, this.z / scalar);
	}
	
	// Faster than length()
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}
	
	// Returns exact length
	public double length() {
		return Math.sqrt(this.lengthSquared());
	}
	
	// Normalize vector
	public Vector normalized() {
		return this.times(1 / this.length());
	}
	
	// Cross product
	public Vector cross(Vector other) {
		return new Vector(
			this.y * other.z - this.z * other.y,
			this.z * other.x - this.x * other.z,
			this.x * other.y - this.y * other.x
		);
	}
	
	// Dot product
	public double dot(Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}
	
	// Get orthagonal vector
	public Vector getOrthagonal() {
		Vector other;
		if(this.y != 0 || this.z != 0) {
			other = new Vector(1, 0, 0);
		} else {
			other = new Vector(0, 1, 0);
		}
		return this.cross(other);
	}
	
	// Convert to string for debugging
	public String toString() {
		return new String("(" + this.x + ", " + this.y + ", " + this.z + ")");
	}
	
	// Convert spherical coordinates to vector
	public static Vector fromSpherical(double azimuth, double inclination) {
		return new Vector(
			Math.sin(inclination) * Math.cos(azimuth),
			Math.cos(inclination),
			Math.sin(inclination) * Math.sin(azimuth)
		);
	}
	
	// Generate uniformly distributed vector in unit sphere
	public static Vector uniformInSphere() {
		double azimuth = 2 * Math.PI * Math.random();
		double inclination = Math.acos(1 - 2 * Math.random());		// This step is necessary or else points clump at the poles
		return Vector.fromSpherical(azimuth, inclination);
	}
	
	//  Generate uniformly distributed vector in unit hemisphere
	public static Vector uniformInHemisphere() {
		Vector result = Vector.uniformInSphere();
		result.y = Math.abs(result.y);
		return result;
	}
	
	public Vector sample(double u, double v) {
		return this;
	}
	
	public static Vector localToWorldCoords(Vector vector, Vector bvx, Vector bvy, Vector bvz) {
		return new Vector(
			vector.x * bvx.x + vector.y * bvy.x + vector.z * bvz.x,
			vector.x * bvx.y + vector.y * bvy.y + vector.z * bvz.y,
			vector.x * bvx.z + vector.y * bvy.z + vector.z * bvz.z
		);
	}
	
}
