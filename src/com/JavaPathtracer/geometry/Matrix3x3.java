package com.JavaPathtracer.geometry;

public class Matrix3x3 {

	public double factors[];
	
	public Matrix3x3() {
		factors = new double[9];
		for(int r = 0; r < 3; r++) {
			for(int c = 0; c < 3; c++) {
				factors[r * 3 + c] = r == c ? 1 : 0;
			}
		}
	}
	
	public Matrix3x3(double[] factors) {
		if(factors.length != 9) throw new IllegalArgumentException("Matrix must have 9 factors.");
		this.factors = factors;
	}
	
	public Vector transform(Vector vector) {
		return new Vector(
			vector.x * factors[0] + vector.y * factors[1] + vector.z * factors[2],
			vector.x * factors[3] + vector.y * factors[4] + vector.z * factors[5],
			vector.x * factors[6] + vector.y * factors[7] + vector.z * factors[8]
		);
	}
	
	public double determinant() {
		return factors[0] * (factors[4] * factors[8] - factors[5] * factors[7]) -
			   factors[1] * (factors[3] * factors[8] - factors[5] * factors[6]) +
			   factors[2] * (factors[3] * factors[7] - factors[4] * factors[6]);
	}
	
	public Matrix3x3 inverse() {
		double invdet = 1 / this.determinant();
		return new Matrix3x3(new double[] {
			 invdet * (factors[4] * factors[8] - factors[5] * factors[7]), -invdet * (factors[1] * factors[8] - factors[2] * factors[7]),  invdet * (factors[1] * factors[5] - factors[2] * factors[4]),
			-invdet * (factors[3] * factors[8] - factors[5] * factors[6]),  invdet * (factors[0] * factors[8] - factors[2] * factors[6]), -invdet * (factors[0] * factors[5] - factors[2] * factors[3]),
			 invdet * (factors[3] * factors[7] - factors[4] * factors[6]), -invdet * (factors[0] * factors[7] - factors[1] * factors[6]),  invdet * (factors[0] * factors[4] - factors[1] * factors[3])
		});
	}
	
}
