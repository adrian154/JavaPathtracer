package com.JavaPathtracer.geometry;

// 3D rotation matrix
// Fixed 4x4 size
public class Matrix {

	public double factors[];
	
	// No args constructor initializes the matrix as an identity matrix
	public Matrix() {
		factors = new double[16];
		for(int r = 0; r < 4; r++) {
			for(int c = 0; c < 4; c++) {
				factors[r * 4 + c] = r == c ? 1 : 0;
			}
		}
	}
	
	public Matrix(double[] factors) {
		if(factors.length != 16) throw new IllegalArgumentException("Matrix must have 16 factors.");
		this.factors = factors;
	}
	
	public static Matrix Translate(double x, double y, double z) {
		Matrix result = new Matrix();
		result.factors[12] = x;
		result.factors[13] = y;
		result.factors[14] = z;
		return result;
	}
	
	public static Matrix Translate(Vector translate) {
		return Matrix.Translate(translate.x, translate.y, translate.z);
	}
	
	public static Matrix Scale(double x, double y, double z) {
		Matrix result = new Matrix();
		result.factors[0] = x;
		result.factors[5] = y;
		result.factors[10] = z;
		return result;
	}
	
	public static Matrix Scale(Vector scale) {
		return Matrix.Scale(scale.x, scale.y, scale.z);
	}
	
	public static Matrix RotateX(double angle) {
		Matrix result = new Matrix();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		result.factors[4] = cos;
		result.factors[5] = sin;
		result.factors[8] = -sin;
		result.factors[9] = cos;
		return result;
	}
	
	public static Matrix RotateY(double angle) {
		Matrix result = new Matrix();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		result.factors[1] = cos;
		result.factors[9] = sin;
		result.factors[0] = -sin;
		result.factors[8] = cos;
		return result;
	}
	
	public static Matrix RotateZ(double angle) {
		Matrix result = new Matrix();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		result.factors[5] = cos;
		result.factors[1] = sin;
		result.factors[4] = -sin;
		result.factors[0] = cos;
		return result;
	}
	
	public Vector transform(Vector vector) {
		return new Vector(
			vector.x * factors[0] + vector.y * factors[1] + vector.z * factors[2] + factors[3],
			vector.x * factors[4] + vector.y * factors[5] + vector.z * factors[6] + factors[7],
			vector.x * factors[8] + vector.y * factors[9] + vector.z * factors[10] + factors[11]
		);
	}
	
	// this code will make you cry
	public Matrix multiply(Matrix other) {
		return new Matrix(new double[] {
			factors[0] * other.factors[0] + factors[1] * other.factors[4] + factors[2] * other.factors[8] + factors[3] * other.factors[12],
			factors[0] * other.factors[1] + factors[1] * other.factors[5] + factors[2] * other.factors[9] + factors[3] * other.factors[13],
			factors[0] * other.factors[2] + factors[1] * other.factors[6] + factors[2] * other.factors[10] + factors[3] * other.factors[14],
			factors[0] * other.factors[3] + factors[1] * other.factors[7] + factors[2] * other.factors[11] + factors[3] * other.factors[15],
			factors[4] * other.factors[0] + factors[5] * other.factors[4] + factors[6] * other.factors[8] + factors[7] * other.factors[12],
			factors[4] * other.factors[1] + factors[5] * other.factors[5] + factors[6] * other.factors[9] + factors[7] * other.factors[13],
			factors[4] * other.factors[2] + factors[5] * other.factors[6] + factors[6] * other.factors[10] + factors[7] * other.factors[14],
			factors[4] * other.factors[3] + factors[5] * other.factors[7] + factors[6] * other.factors[11] + factors[7] * other.factors[15],
			factors[8] * other.factors[0] + factors[9] * other.factors[4] + factors[10] * other.factors[8] + factors[11] * other.factors[12],
			factors[8] * other.factors[1] + factors[9] * other.factors[5] + factors[10] * other.factors[9] + factors[11] * other.factors[13],
			factors[8] * other.factors[2] + factors[9] * other.factors[6] + factors[10] * other.factors[10] + factors[11] * other.factors[14],
			factors[8] * other.factors[3] + factors[9] * other.factors[7] + factors[10] * other.factors[11] + factors[11] * other.factors[15],
			factors[12] * other.factors[0] + factors[13] * other.factors[4] + factors[14] * other.factors[8] + factors[15] * other.factors[12],
			factors[12] * other.factors[1] + factors[13] * other.factors[5] + factors[14] * other.factors[9] + factors[15] * other.factors[13],
			factors[12] * other.factors[2] + factors[13] * other.factors[6] + factors[14] * other.factors[10] + factors[15] * other.factors[14],
			factors[12] * other.factors[3] + factors[13] * other.factors[7] + factors[14] * other.factors[11] + factors[15] * other.factors[15]		
		});
	}
	
	public double get(int r, int c) {
		return factors[r * 4 + c];
	}
	
}
