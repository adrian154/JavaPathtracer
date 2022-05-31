package com.JavaPathtracer.geometry.mesh;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Vector;

// Bounding box attached to a triangle. Used in BVH construction.
public class TriangleBoundingBox extends BoundingBox {

	public final int face;

	public TriangleBoundingBox(Vector min, Vector max, int face) {
		super(min, max);
		this.face = face;
	}

	public static TriangleBoundingBox create(MeshGeometry mesh, int face) {
		
		Vector v0 = mesh.vertexes[mesh.faces[face * 3]];
		Vector v1 = mesh.vertexes[mesh.faces[face * 3 + 1]];
		Vector v2 = mesh.vertexes[mesh.faces[face * 3 + 2]];
		
		return new TriangleBoundingBox(
			BoundingBox.min(v0, BoundingBox.min(v1, v2)),
			BoundingBox.max(v0, BoundingBox.max(v1, v2)),
			face
		);
		
	}

}
