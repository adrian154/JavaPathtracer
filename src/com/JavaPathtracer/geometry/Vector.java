package com.JavaPathtracer.geometry;

import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.material.Sampleable;

// 3D vector class
// Implements Sampleable so you can use Vectors as colors, kind of janky but oh well
public class Vector implements Sampleable {

	// Components
	public double x, y, z;

	public static final Vector ZERO = new Vector(0.0, 0.0, 0.0);
	public static final Vector ONE = new Vector(1.0, 1.0, 1.0);
	
	public Vector() {
		this(0);
	}
	
	public Vector(double val) {
		this.x = val;
		this.y = val;
		this.z = val;
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

	public Vector plus(double scalar) {
		return new Vector(this.x + scalar, this.y + scalar, this.z + scalar);
	}
	
	public Vector iadd(Vector other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public Vector minus(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	public Vector isub(Vector other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	// Direct multiplication, should only be used on colors
	public Vector times(Vector other) {
		return new Vector(this.x * other.x, this.y * other.y, this.z * other.z);
	}

	public Vector imul(Vector other) {
		this.x *= other.x;
		this.y *= other.y;
		this.z *= other.z;
		return this;
	}

	// Scalar multiplication
	public Vector times(double scalar) {
		return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	public Vector imul(double scalar) {
		this.x *= scalar;
		this.y *= scalar;
		this.z *= scalar;
		return this;
	}

	// Scalar division
	public Vector divBy(double scalar) {
		return new Vector(this.x / scalar, this.y / scalar, this.z / scalar);
	}

	public Vector idiv(double scalar) {
		this.x /= scalar;
		this.y /= scalar;
		this.z /= scalar;
		return this;
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

	public Vector normalize() {
		double length = this.length();
		this.x /= length;
		this.y /= length;
		this.z /= length;
		return this;
	}

	// Cross product
	public Vector cross(Vector other) {
		return new Vector(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z,
				this.x * other.y - this.y * other.x);
	}

	// Dot product
	public double dot(Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	// Get orthagonal vector
	public Vector getOrthagonal() {
		Vector other;
		if (this.y != 0 || this.z != 0) {
			other = new Vector(1, 0, 0);
		} else {
			other = new Vector(0, 1, 0);
		}
		return this.cross(other);
	}

	// Convert to string for debugging
	@Override
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

	public static Vector fromSpherical(Vector in) {
		return Vector.fromSpherical(in.x, in.y);
	}

	// this is kind of scuffed...
	public Vector toSpherical() {
		return new Vector(
			Math.atan2(this.z, this.x),
			Math.acos(this.y / Math.sqrt(this.x * this.x + this.z * this.z)),
			0.0
		);
	}

	// Generate uniformly distributed vector in unit sphere
	public static Vector uniformInSphere() {
		return Vector.fromSpherical(
			ThreadLocalRandom.current().nextDouble() * 2 * Math.PI,
			Math.acos(1 - 2 * ThreadLocalRandom.current().nextDouble())
		);
	}
	
	public static Vector cosineWeightedInHemisphere() {
		return Vector.fromSpherical(
			ThreadLocalRandom.current().nextDouble() * 2 * Math.PI,
			ThreadLocalRandom.current().nextDouble() * Math.PI / 2
		);
	}

	// Generate uniformly distributed vector in unit hemisphere
	public static Vector uniformInHemisphere() {
		return Vector.fromSpherical(
			Math.random() * 2 * Math.PI,
			Math.acos(Math.random())
		);
	}

	@Override
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
