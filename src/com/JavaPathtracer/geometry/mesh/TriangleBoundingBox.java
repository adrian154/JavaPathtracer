package com.JavaPathtracer.geometry.mesh;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Vector;

// Bounding box attached to a triangle. Used in BVH construction.
public class TriangleBoundingBox extends BoundingBox {

	public final int face;
	
	public static double minOf3(double a, double b, double c) { return Math.min(a, Math.min(b, c)); }
	public static double maxOf3(double a, double b, double c) { return Math.max(a, Math.max(b, c)); }
	
	public TriangleBoundingBox(Vector min, Vector max, int face) {
		super(min, max);
		this.face = face;
	}

	public static TriangleBoundingBox create(Mesh mesh, int face) {
		
		Vector v0 = mesh.vertexes[mesh.faces[face * 3]];
		Vector v1 = mesh.vertexes[mesh.faces[face * 3 + 1]];
		Vector v2 = mesh.vertexes[mesh.faces[face * 3 + 2]];
		
		return new TriangleBoundingBox(
			new Vector(
				minOf3(v0.x, v1.x, v2.x),
				minOf3(v0.y, v1.y, v2.y),
				minOf3(v0.z, v1.z, v2.z)
			),
			new Vector(
				maxOf3(v0.x, v1.x, v2.x),
				maxOf3(v0.y, v1.y, v2.y),
				maxOf3(v0.z, v1.z, v2.z)
			),
			face
		);
		
	}

}
