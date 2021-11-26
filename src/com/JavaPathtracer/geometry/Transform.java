package com.JavaPathtracer.geometry;

public class Transform {

	private Matrix4x4 matrix;
	private Matrix4x4 inverse;
	private Matrix4x4 inverseTransposed;
	
	public Transform() {
		this(new Matrix4x4());
	}
	
	public Transform(Matrix4x4 matrix) {
		this.matrix = matrix;
		this.inverse = matrix.inverse();
		this.inverseTransposed = this.inverse.transpose();
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

	@Override
	public String toString() {
		return this.matrix.toString();
	}
	
}
