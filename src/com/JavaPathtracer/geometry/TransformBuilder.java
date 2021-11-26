package com.JavaPathtracer.geometry;

public class TransformBuilder {

	private Matrix4x4 matrix;
	
	public TransformBuilder() {
		this.matrix = new Matrix4x4();
	}
	
	public TransformBuilder applyTransform(Matrix4x4 transform) {
		matrix = matrix.multiply(transform);
		return this;
	}
	
	public TransformBuilder swizzleXZY() {
		return this.applyTransform(new Matrix4x4(new double[] {
			1, 0, 0, 0,
			0, 0, 1, 0,
			0, 1, 0, 0,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder swizzleYXZ() {
		return this.applyTransform(new Matrix4x4(new double[] {
			0, 1, 0, 0,
			1, 0, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder swizzleYZX() {
		return this.applyTransform(new Matrix4x4(new double[] {
			0, 1, 0, 0,
			0, 0, 1, 0,
			1, 0, 0, 0,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder swizzleZXY() {
		return this.applyTransform(new Matrix4x4(new double[] {
			0, 0, 1, 0,
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder swizzleZYX() {
		return this.applyTransform(new Matrix4x4(new double[] {
			0, 0, 1, 0,
			0, 1, 0, 0,
			1, 0, 0, 0,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder toCoordinateSpace(Vector bvx, Vector bvy, Vector bvz) {
		return this.applyTransform(new Matrix4x4(new double[] {
			bvx.x, bvy.x, bvz.x, 0,
			bvx.y, bvy.y, bvz.z, 0,
			bvx.z, bvy.z, bvz.z, 0,
			0,     0,     0,     1
		}));
	}
	
	public TransformBuilder translate(double x, double y, double z) {
		return this.applyTransform(new Matrix4x4(new double[] {
			1, 0, 0, x,
			0, 1, 0, y,
			0, 0, 1, z,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder translate(Vector vector) {
		return this.translate(vector.x, vector.y, vector.z);
	}
	
	public TransformBuilder scale(double x, double y, double z) {
		return this.applyTransform(new Matrix4x4(new double[] {
			x, 0, 0, 0,
			0, y, 0, 0,
			0, 0, z, 0,
			0, 0, 0, 1
		}));
	}
	
	public TransformBuilder scale(double scale) {
		return this.scale(scale, scale, scale);
	}
	
	public TransformBuilder rotateX(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return this.applyTransform(new Matrix4x4(new double[] {
			1,  0,   0,   0,
			0,  cos, sin, 0,
			0, -sin, cos, 0,
			0,  0,   0,   1
		}));
	}
	
	public TransformBuilder rotateY(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return this.applyTransform(new Matrix4x4(new double[] {
			cos, 0, -sin, 0,
			0,   1,  0,   0,
			sin, 0,  cos, 0,
			0,   0,  0,   1
		}));
	}
	
	public TransformBuilder rotateZ(double angle) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return this.applyTransform(new Matrix4x4(new double[] {
			cos, -sin, 0, 0,
			sin,  cos, 0, 0,
			0,    0,   1, 0,
			0,    0,   0, 1
		}));
	}
	
	public Transform build() {
		return new Transform(matrix);
	}
	
}
