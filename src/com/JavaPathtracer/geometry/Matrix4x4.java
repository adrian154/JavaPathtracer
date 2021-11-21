package com.JavaPathtracer.geometry;

// Fixed-size 4x4 matrix
public class Matrix4x4 {

	public final double factors[];

	// when constructed with no arguments, create an identity matrix
	public Matrix4x4() {
		factors = new double[16];
		for (int r = 0; r < 4; r++) {
			for (int c = 0; c < 4; c++) {
				factors[r * 4 + c] = r == c ? 1 : 0;
			}
		}
	}

	public Matrix4x4(double[] factors) {
		if (factors.length != 16)
			throw new IllegalArgumentException("Matrix must have 16 factors.");
		this.factors = factors;
	}

	public Matrix4x4 Translate(double x, double y, double z) {
		Matrix4x4 transform = new Matrix4x4();
		transform.factors[3] = x;
		transform.factors[7] = y;
		transform.factors[11] = z;
		return this.multiply(transform);
	}

	public Matrix4x4 Translate(Vector translate) {
		return this.Translate(translate.x, translate.y, translate.z);
	}

	public Matrix4x4 Scale(double x, double y, double z) {
		Matrix4x4 transform = new Matrix4x4();
		transform.factors[0] = x;
		transform.factors[5] = y;
		transform.factors[10] = z;
		return this.multiply(transform);
	}

	public Matrix4x4 Scale(Vector scale) {
		return this.Scale(scale.x, scale.y, scale.z);
	}

	public Matrix4x4 Scale(double scale) {
		return this.Scale(scale, scale, scale);
	}

	public Matrix4x4 RotateX(double angle) {
		Matrix4x4 transform = new Matrix4x4();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		transform.factors[5] = cos;
		transform.factors[6] = -sin;
		transform.factors[7] = sin;
		transform.factors[8] = cos;
		return this.multiply(transform);
	}

	public Matrix4x4 RotateY(double angle) {
		Matrix4x4 transform = new Matrix4x4();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		transform.factors[0] = cos;
		transform.factors[2] = sin;
		transform.factors[8] = -sin;
		transform.factors[10] = cos;
		return this.multiply(transform);
	}

	public Matrix4x4 RotateZ(double angle) {
		Matrix4x4 transform = new Matrix4x4();
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		transform.factors[0] = cos;
		transform.factors[1] = -sin;
		transform.factors[4] = sin;
		transform.factors[5] = cos;
		return this.multiply(transform);
	}

	// w = 1
	public Vector transformPoint(Vector vector) {
		return new Vector(
			vector.x * factors[0] + vector.y * factors[1] + vector.z * factors[2] + factors[3],
			vector.x * factors[4] + vector.y * factors[5] + vector.z * factors[6] + factors[7],
			vector.x * factors[8] + vector.y * factors[9] + vector.z * factors[10] + factors[11]
		);
	}
	
	// w = 0
	public Vector transformVector(Vector vector) {
		return new Vector(
				vector.x * factors[0] + vector.y * factors[1] + vector.z * factors[2],
				vector.x * factors[4] + vector.y * factors[5] + vector.z * factors[6],
				vector.x * factors[8] + vector.y * factors[9] + vector.z * factors[10]
			);
	}

	public Matrix4x4 multiply(Matrix4x4 other) {
		return new Matrix4x4(new double[] {
			factors[0]  * other.factors[0] +  factors[1] * other.factors[4] + factors[2]   * other.factors[8]  + factors[3]  * other.factors[12],
			factors[0]  * other.factors[1] +  factors[1] * other.factors[5] + factors[2]   * other.factors[9]  + factors[3]  * other.factors[13],
			factors[0]  * other.factors[2] +  factors[1] * other.factors[6] + factors[2]   * other.factors[10] + factors[3]  * other.factors[14],
			factors[0]  * other.factors[3] +  factors[1] * other.factors[7] + factors[2]   * other.factors[11] + factors[3]  * other.factors[15],
			factors[4]  * other.factors[0] +  factors[5] * other.factors[4] + factors[6]   * other.factors[8]  + factors[7]  * other.factors[12],
			factors[4]  * other.factors[1] +  factors[5] * other.factors[5] + factors[6]   * other.factors[9]  + factors[7]  * other.factors[13],
			factors[4]  * other.factors[2] +  factors[5] * other.factors[6] + factors[6]   * other.factors[10] + factors[7]  * other.factors[14],
			factors[4]  * other.factors[3] +  factors[5] * other.factors[7] + factors[6]   * other.factors[11] + factors[7]  * other.factors[15],
			factors[8]  * other.factors[0] +  factors[9] * other.factors[4] + factors[10]  * other.factors[8]  + factors[11] * other.factors[12],
			factors[8]  * other.factors[1] +  factors[9] * other.factors[5] + factors[10]  * other.factors[9]  + factors[11] * other.factors[13],
			factors[8]  * other.factors[2] +  factors[9] * other.factors[6] + factors[10]  * other.factors[10] + factors[11] * other.factors[14],
			factors[8]  * other.factors[3] +  factors[9] * other.factors[7] + factors[10]  * other.factors[11] + factors[11] * other.factors[15],
			factors[12] * other.factors[0] + factors[13] * other.factors[4]  + factors[14] * other.factors[8]  + factors[15] * other.factors[12],
			factors[12] * other.factors[1] + factors[13] * other.factors[5]  + factors[14] * other.factors[9]  + factors[15] * other.factors[13],
			factors[12] * other.factors[2] + factors[13] * other.factors[6]  + factors[14] * other.factors[10] + factors[15] * other.factors[14],
			factors[12] * other.factors[3] + factors[13] * other.factors[7]  + factors[14] * other.factors[11] + factors[15] * other.factors[15]
		});
	}
	
	// co-opted from old Mesa code
	public Matrix4x4 inverse() {
	
		double[] inverse = new double[16];

		inverse[0] = factors[5] * factors[10] * factors[15] - factors[5] * factors[11] * factors[14]
				- factors[9] * factors[6] * factors[15] + factors[9] * factors[7] * factors[14]
				+ factors[13] * factors[6] * factors[11] - factors[13] * factors[7] * factors[10];

		inverse[4] = -factors[4] * factors[10] * factors[15] + factors[4] * factors[11] * factors[14]
				+ factors[8] * factors[6] * factors[15] - factors[8] * factors[7] * factors[14]
				- factors[12] * factors[6] * factors[11] + factors[12] * factors[7] * factors[10];

		inverse[8] = factors[4] * factors[9] * factors[15] - factors[4] * factors[11] * factors[13]
				- factors[8] * factors[5] * factors[15] + factors[8] * factors[7] * factors[13]
				+ factors[12] * factors[5] * factors[11] - factors[12] * factors[7] * factors[9];

		inverse[12] = -factors[4] * factors[9] * factors[14] + factors[4] * factors[10] * factors[13]
				+ factors[8] * factors[5] * factors[14] - factors[8] * factors[6] * factors[13]
				- factors[12] * factors[5] * factors[10] + factors[12] * factors[6] * factors[9];

		inverse[1] = -factors[1] * factors[10] * factors[15] + factors[1] * factors[11] * factors[14]
				+ factors[9] * factors[2] * factors[15] - factors[9] * factors[3] * factors[14]
				- factors[13] * factors[2] * factors[11] + factors[13] * factors[3] * factors[10];

		inverse[5] = factors[0] * factors[10] * factors[15] - factors[0] * factors[11] * factors[14]
				- factors[8] * factors[2] * factors[15] + factors[8] * factors[3] * factors[14]
				+ factors[12] * factors[2] * factors[11] - factors[12] * factors[3] * factors[10];

		inverse[9] = -factors[0] * factors[9] * factors[15] + factors[0] * factors[11] * factors[13]
				+ factors[8] * factors[1] * factors[15] - factors[8] * factors[3] * factors[13]
				- factors[12] * factors[1] * factors[11] + factors[12] * factors[3] * factors[9];

		inverse[13] = factors[0] * factors[9] * factors[14] - factors[0] * factors[10] * factors[13]
				- factors[8] * factors[1] * factors[14] + factors[8] * factors[2] * factors[13]
				+ factors[12] * factors[1] * factors[10] - factors[12] * factors[2] * factors[9];

		inverse[2] = factors[1] * factors[6] * factors[15] - factors[1] * factors[7] * factors[14]
				- factors[5] * factors[2] * factors[15] + factors[5] * factors[3] * factors[14]
				+ factors[13] * factors[2] * factors[7] - factors[13] * factors[3] * factors[6];

		inverse[6] = -factors[0] * factors[6] * factors[15] + factors[0] * factors[7] * factors[14]
				+ factors[4] * factors[2] * factors[15] - factors[4] * factors[3] * factors[14]
				- factors[12] * factors[2] * factors[7] + factors[12] * factors[3] * factors[6];

		inverse[10] = factors[0] * factors[5] * factors[15] - factors[0] * factors[7] * factors[13]
				- factors[4] * factors[1] * factors[15] + factors[4] * factors[3] * factors[13]
				+ factors[12] * factors[1] * factors[7] - factors[12] * factors[3] * factors[5];

		inverse[14] = -factors[0] * factors[5] * factors[14] + factors[0] * factors[6] * factors[13]
				+ factors[4] * factors[1] * factors[14] - factors[4] * factors[2] * factors[13]
				- factors[12] * factors[1] * factors[6] + factors[12] * factors[2] * factors[5];

		inverse[3] = -factors[1] * factors[6] * factors[11] + factors[1] * factors[7] * factors[10]
				+ factors[5] * factors[2] * factors[11] - factors[5] * factors[3] * factors[10]
				- factors[9] * factors[2] * factors[7] + factors[9] * factors[3] * factors[6];

		inverse[7] = factors[0] * factors[6] * factors[11] - factors[0] * factors[7] * factors[10]
				- factors[4] * factors[2] * factors[11] + factors[4] * factors[3] * factors[10]
				+ factors[8] * factors[2] * factors[7] - factors[8] * factors[3] * factors[6];

		inverse[11] = -factors[0] * factors[5] * factors[11] + factors[0] * factors[7] * factors[9]
				+ factors[4] * factors[1] * factors[11] - factors[4] * factors[3] * factors[9]
				- factors[8] * factors[1] * factors[7] + factors[8] * factors[3] * factors[5];

		inverse[15] = factors[0] * factors[5] * factors[10] - factors[0] * factors[6] * factors[9]
				- factors[4] * factors[1] * factors[10] + factors[4] * factors[2] * factors[9]
				+ factors[8] * factors[1] * factors[6] - factors[8] * factors[2] * factors[5];

		double determinant = 1 / (factors[0] * inverse[0] +
 				                  factors[1] * inverse[4] + 
				                  factors[2] * inverse[8] + 
				                  factors[3] * inverse[12]);
		
		for(int i = 0; i < 16; i++) inverse[i] /= determinant;
		return new Matrix4x4(inverse);
		
	}

	public double get(int r, int c) {
		return factors[r * 4 + c];
	}

}
