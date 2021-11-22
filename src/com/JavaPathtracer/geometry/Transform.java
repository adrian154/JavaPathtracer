package com.JavaPathtracer.geometry;

public class Transform {

	private Matrix4x4 matrix;
	private Matrix4x4 inverse;
	private Matrix4x4 inverseTransposed;
	
	public Transform() {
		this.matrix = new Matrix4x4();
	}
	
	public Matrix4x4 getMatrix() {
		return matrix;
	}
	
	public Transform translate(double x, double y, double z) {
		Matrix4x4 transform = new Matrix4x4();
		transform.factors[3]  = x;
		transform.factors[7]  = y;
		transform.factors[11] = z;
		matrix = matrix.multiply(transform);
		return this;
	}
	
	public Transform translate(Vector vector) {
		return this.translate(vector.x, vector.y, vector.z);
	}
	
	public Transform scale(double x, double y, double z) {
		Matrix4x4 transform = new Matrix4x4();
		transform.factors[0]  = x;
		transform.factors[5]  = y;
		transform.factors[10] = z;
		matrix = matrix.multiply(transform);
		return this;
	}
	
	public Transform scale(double scale) {
		return this.scale(scale, scale, scale);
	}
	
	public Transform rotateX(double angle) {
		Matrix4x4 transform = new Matrix4x4();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		transform.factors[5] = cos;
		transform.factors[6] = -sin;
		transform.factors[7] = sin;
		transform.factors[8] = cos;
		matrix = matrix.multiply(transform);
		return this;
	}
	
	public Transform rotateY(double angle) {
		Matrix4x4 transform = new Matrix4x4();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		transform.factors[0] = cos;
		transform.factors[2] = sin;
		transform.factors[8] = -sin;
		transform.factors[10] = cos;
		matrix = matrix.multiply(transform);
		return this;
	}
	
	public Transform rotateZ(double angle) {
		Matrix4x4 transform = new Matrix4x4();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		transform.factors[0] = cos;
		transform.factors[1] = -sin;
		transform.factors[4] = sin;
		transform.factors[5] = cos;
		matrix = matrix.multiply(transform);
		return this;
	}
	
	public Transform complete() {
		inverse = matrix.inverse();
		inverseTransposed = inverse.transpose();	
		return this;
	}
	
	public Vector transformPoint(Vector vector) {
		return matrix.transformPoint(vector);
	}
	
	public Vector transformVector(Vector vector) {
		return matrix.transformVector(vector);
	}
	
	public Vector inversePoint(Vector vector) {
		return inverse.transformPoint(vector);
	}
	
	public Vector inverseVector(Vector vector) {
		return inverse.transformVector(vector);
	}
	
	public Vector transformNormal(Vector vector) {
		return inverseTransposed.transformVector(vector);
	}
	
}
