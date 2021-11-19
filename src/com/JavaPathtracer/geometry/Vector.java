package com.JavaPathtracer.geometry;

import java.util.concurrent.ThreadLocalRandom;

import com.JavaPathtracer.pattern.Sampleable;

// 3D vector class
// All methods are immutable. After much thinking I have decided that mutable methods...
// * tend to pollute the code with poorly-mixing syntaxes
// * probably don't actually increase performance because the garbage collector is smart enough to eliminate unnecessary allocations

public class Vector implements Sampleable {

	// Components
	public final double x, y, z;

	public static final Vector ZERO = new Vector(0.0, 0.0, 0.0);
	public static final Vector ONE = new Vector(1.0, 1.0, 1.0);
	public static final Vector X = new Vector(1.0, 0.0, 0.0);
	public static final Vector Y = new Vector(0.0, 1.0, 0.0);
	public static final Vector Z = new Vector(0.0, 0.0, 1.0);
	
	// rgb components since we also store colors as vectors
	public static final Vector R = X;
	public static final Vector G = Y;
	public static final Vector B = Z;
	
	// rgb values are normalized to [0..1]
	public Vector(int rgb) {
		this(
			((rgb & 0xFF0000) >> 16) / 255.0,
			((rgb & 0x00FF00) >> 8) / 255.0,
			(rgb & 0xFF) / 255.0
		);
	}
	
	public Vector() {
		this(0);
	}
	
	public Vector(double value) {
		this.x = value;
		this.y = value;
		this.z = value;
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		this.z = 0;
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// allows iteration over components
	public double get(int component) {
		return component == 0 ? x :
			   component == 1 ? y :
			                    z;
	}
	
	public Vector reverse() {
		return this.times(-1);
	}
	
	// reverse vectors not facing the input
	public Vector facing(Vector vec) {
		return this.dot(vec) > 0 ? this.reverse() : this;
	}

	public Vector plus(Vector other) {
		return new Vector(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	public Vector plus(double scalar) {
		return new Vector(this.x + scalar, this.y + scalar, this.z + scalar);
	}

	public Vector minus(Vector other) {
		return new Vector(this.x - other.x, this.y - other.y, this.z - other.z);
	}

	// directly multiply the components of the vector, used to do things like applying albedo to incoming light
	// the fancy term is a "Hadamard product"
	public Vector times(Vector other) {
		return new Vector(this.x * other.x, this.y * other.y, this.z * other.z);
	}

	// scalar multiplication
	public Vector times(double scalar) {
		return new Vector(this.x * scalar, this.y * scalar, this.z * scalar);
	}

	// scalar division
	public Vector divBy(double scalar) {
		return new Vector(this.x / scalar, this.y / scalar, this.z / scalar);
	}

	// avoid an expensive sqrt() call when unnecessary
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	// euclidena length
	public double length() {
		return Math.sqrt(this.lengthSquared());
	}

	// normalize vector
	public Vector normalize() {
		return this.times(1 / this.length());
	}

	// cross product
	public Vector cross(Vector other) {
		return new Vector(
			this.y * other.z - this.z * other.y,
			this.z * other.x - this.x * other.z,
			this.x * other.y - this.y * other.x
		);
	}

	// dot product
	public double dot(Vector other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	// get an arbitrary orthagonal vector
	// this is done by simply finding the cross product with another suitable vector
	public Vector getOrthagonal() {
		Vector other = (this.y != 0 || this.z != 0) ? new Vector(1, 0, 0) : new Vector(0, 1, 0);
		return this.cross(other).normalize();
	}

	// convert to string for debugging
	@Override
	public String toString() {
		return String.format("(%.02f, %.02f, %.02f)", x, y, z);
	}

	// convert spherical coordinates to vector
	public static Vector fromSpherical(double azimuth, double inclination) {
		double sinInc = Math.sin(inclination);
		return new Vector(
			sinInc * Math.cos(azimuth),
			Math.cos(inclination),
			sinInc * Math.sin(azimuth)
		);
	}

	public static Vector fromSpherical(Vector in) {
		return Vector.fromSpherical(in.x, in.y);
	}

	// this method is really slow (inverse trig is even slower than regular trig)
	// avoid unless necessary!
	public Vector toSpherical() {
		return new Vector(
			Math.atan2(this.z, this.x),
			Math.acos(this.y / Math.sqrt(this.x * this.x + this.z * this.z)),
			0.0
		);
	}
	
	public static Vector cosineWeightedInHemisphere() {
		return Vector.fromSpherical(
			ThreadLocalRandom.current().nextDouble() * 2 * Math.PI,
			ThreadLocalRandom.current().nextDouble() * Math.PI / 2
		);
	}

	// Generate uniformly distributed vector in unit hemisphere
	// naively picking a random inclination results in points clumping at the poles
	public static Vector uniformInHemisphere() {
		return Vector.fromSpherical(
			Math.random() * 2 * Math.PI,
			Math.acos(Math.random())
		);
	}
	
	private int clamp(double value) {
		return (int)Math.min(Math.max(value, 255), 0);
	}
	
	public int toRGB() {
		return (int)clamp(x) << 16 | (int)clamp(y) << 8 | (int)clamp(z);
	}
	
	// for use with RGB colors
	public String toHexTriplet() {
		return String.format("#%02x%02x%02x", (int)(x * 255), (int)(y * 255), (int)(z * 255));
	}
	
	// vectors can be used as flat textures
	@Override
	public Vector sample(Vector textureCoord) {
		return this;
	}

	// convert vector in a local space to world space
	// multiply each component by its corresponding basis vector and sum up the result
	public Vector fromCoordinateSpace(Vector bvx, Vector bvy, Vector bvz) {
		return new Vector(
			this.x * bvx.x + this.y * bvy.x + this.z * bvz.x,
			this.x * bvx.y + this.y * bvy.y + this.z * bvz.y,
			this.x * bvx.z + this.y * bvy.z + this.z * bvz.z
		);
	}

}
