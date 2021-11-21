package com.JavaPathtracer.geometry.mesh;

import java.util.List;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

// A container for triangle geometry. Mesh itself does not implement Shape, raytracing is deferred to acceleration structures.
public class MeshGeometry {

	public int[] faces;
	public int[] normalIndices;
	public int[] texCoordIndices;
	
	public Vector[] vertexes;
	public Vector[] vertexNormals;
	public Vector[] textureCoordinates;
	
	public String[] materialIDs;
	
	public MeshGeometry(
		List<Vector> vertexes,
		List<Vector> vertexNormals,
		List<Vector> textureCoordinates,
		List<Integer> faces,
		List<Integer> faceNormIndices,
		List<Integer> faceTexCoordIndices,
		List<String> materialIDs
	) {
		
		this.vertexes = vertexes.toArray(new Vector[0]);
		this.vertexNormals = vertexNormals.size() > 0 ? vertexNormals.toArray(new Vector[0]) : null;
		this.faces = faces.stream().mapToInt(Integer::valueOf).toArray();
		this.normalIndices = faceNormIndices.stream().mapToInt(Integer::valueOf).toArray();
		this.texCoordIndices = faceTexCoordIndices.stream().mapToInt(Integer::valueOf).toArray();
		this.textureCoordinates = textureCoordinates.size() > 0 ? textureCoordinates.toArray(new Vector[0]) : null;
		this.materialIDs = materialIDs.toArray(new String[0]);

	}
	
	// Moller-Trumbore triangle intersection algorithm
	// The barycentric coordinates of the hit within the triangle is returned in Hit.textureCoordinates for convenience
	// If texture mapping data is available, it is used to create the actual texture coordinate in .intersect()
	public Hit intersectTri(Ray ray, int face) {

		Vector v0 = vertexes[faces[face * 3]];
		Vector v1 = vertexes[faces[face * 3 + 1]];
		Vector v2 = vertexes[faces[face * 3 + 2]];
		
		Vector edge1 = v1.minus(v0);
		Vector edge2 = v2.minus(v0);
		Vector h = ray.direction.cross(edge2);

		double a = edge1.dot(h);
		if (a > -Pathtracer.EPSILON && a < Pathtracer.EPSILON) {
			return Hit.MISS;
		}

		double f = 1 / a;
		Vector s = ray.origin.minus(v0);
		double u = f * s.dot(h);
		if (u < 0.0 || u > 1.0)
			return Hit.MISS;

		Vector q = s.cross(edge1);
		double v = f * ray.direction.dot(q);
		if (v < 0.0 || u + v > 1.0)
			return Hit.MISS;

		double t = f * edge2.dot(q);
		if (t > Pathtracer.EPSILON) {
			
			Vector normal;
			
			if(vertexNormals == null) {
			
				// no vertex normals so can't interpolate :(
				// treat triangle as flat
				normal = edge1.cross(edge2);
				
			} else {

				// interpolate between vertex normals based on barycentric coordinates
				Vector norm1 = vertexNormals[normalIndices[face * 3]];
				Vector norm2 = vertexNormals[normalIndices[face * 3 + 1]];
				Vector norm3 = vertexNormals[normalIndices[face * 3 + 2]];
				normal = norm1.times(1 - u - v).plus(norm2.times(u)).plus(norm3.times(v));
				
			}
		
			// TODO: actually calculate tangent vector instead of just using a null vector
			normal = normal.normalize().facing(ray.direction);
			return new MeshHit(ray, ray.getPoint(t), normal, null, t, new Vector(u, v, 0.0), face);
			
		} else {
			return Hit.MISS;
		}

	}
	
	public Hit intersect(Ray ray, int[] prims) {
		
		Hit nearest = Hit.MISS;
		int nearestTri = 0;
		
		for (int i: prims) {

			Hit cur = intersectTri(ray, i);
			if (cur.hit && cur.distance < nearest.distance) {
				nearest = cur;
				nearestTri = i;
			}

		}

		// apply texture coordinates
		if (nearest.hit) {

			if(textureCoordinates != null) {
				Vector tex0 = textureCoordinates[texCoordIndices[nearestTri * 3]];
				Vector tex1 = textureCoordinates[texCoordIndices[nearestTri * 3 + 1]];
				Vector tex2 = textureCoordinates[texCoordIndices[nearestTri * 3 + 2]];
				nearest.textureCoord = tex1.plus((tex1.minus(tex0).times(nearest.textureCoord.x)).plus(tex2.minus(tex0).times(nearest.textureCoord.y)));
			}
			
			return nearest;
			
		}
		
		return Hit.MISS;
		
	}
	
}
