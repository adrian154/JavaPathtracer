package com.JavaPathtracer.geometry.mesh;

import java.util.List;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.Stopwatch;
import com.JavaPathtracer.geometry.FiniteShape;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.MeshHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;

public class MeshGeometryContainer implements FiniteShape {

	public int[] faces;
	public int[] faceNormIndices;
	public int[] faceTexCoordIndices;
	public Vector[] vertexes;
	public Vector[] vertexNormals;
	public Vector[] textureCoordinates;
	
	protected BVHNode BVHRoot;
	
	public MeshGeometryContainer(
		List<Vector> vertexes,
		List<Vector> vertexNormals,
		List<Vector> textureCoordinates,
		List<Integer> faces,
		List<Integer> faceNormIndices,
		List<Integer> faceTexCoordIndices
	) {
		
		this.vertexes = vertexes.toArray(new Vector[0]);
		this.vertexNormals = vertexNormals.size() > 0 ? vertexNormals.toArray(new Vector[0]) : null;
		this.faces = faces.stream().mapToInt(Integer::valueOf).toArray();
		this.faceNormIndices = faceNormIndices.stream().mapToInt(Integer::valueOf).toArray();
		this.faceTexCoordIndices = faceTexCoordIndices.stream().mapToInt(Integer::valueOf).toArray();
		this.textureCoordinates = textureCoordinates.size() > 0 ? textureCoordinates.toArray(new Vector[0]) : null;

		Stopwatch stopwatch = new Stopwatch("BVHBuild");
		BVHRoot = new BVHNode(this);
		stopwatch.stop();
		
	}
	
	// Moller-Trumbore triangle intersection algorithm
	// Uses barycentric coordinates to test triangle intersection
	// (...as well as some general math trickery)
	// THe barycentric coordinates are returned as the texture coordinates for
	// convenience, but beware they are not the same thing!!
	public Hit intersectTri(Ray ray, int which) {

		Vector v0 = vertexes[faces[which * 3]];
		Vector v1 = vertexes[faces[which * 3 + 1]];
		Vector v2 = vertexes[faces[which * 3 + 2]];
		
		Vector edge1 = v1.minus(v0);
		Vector edge2 = v2.minus(v0);
		Vector h = ray.direction.cross(edge2);

		double a = edge1.dot(h);
		if (a > -Raytracer.EPSILON && a < Raytracer.EPSILON) {
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
		if (t > Raytracer.EPSILON) {
			
			Vector normal;
			
			if(vertexNormals == null) {
			
				// No vertex normals = can't interpolate :(
				// Assume tri is flat
				normal = edge1.cross(edge2);
				
			} else {

				// interpolate between vertex normals based on barycentric coordinates
				Vector norm1 = vertexNormals[faceNormIndices[which * 3]];
				Vector norm2 = vertexNormals[faceNormIndices[which * 3 + 1]];
				Vector norm3 = vertexNormals[faceNormIndices[which * 3 + 2]];
				normal = norm1.times(1 - u - v).plus(norm2.times(u)).plus(norm3.times(v));
				
			}
		
			normal.normalize();
			if (normal.dot(ray.direction) > 0) {
				normal.invert();
			}
		
			return new MeshHit(ray.getPoint(t), normal, t, new Vector(u, v, 0.0), which);
		} else {
			return Hit.MISS;
		}

	}
	
	public Hit intersect(Ray ray, int[] prims) {
		
		Hit nearest = Hit.MISS;
		int nearestIndex = 0;
		for (int i: prims) {

			Hit cur = intersectTri(ray, i);
			if (cur.distance < nearest.distance) {
				nearest = cur;
				nearestIndex = i;
			}

		}

		if (nearest.hit && textureCoordinates != null) {
			Vector tex1 = textureCoordinates[faceTexCoordIndices[nearestIndex * 3]];
			Vector tex2 = textureCoordinates[faceTexCoordIndices[nearestIndex * 3 + 1]];
			Vector tex3 = textureCoordinates[faceTexCoordIndices[nearestIndex * 3 + 2]];
			nearest.textureCoordinates = tex1.plus((tex2.minus(tex1).times(nearest.textureCoordinates.x))
					.plus(tex3.minus(tex1).times(nearest.textureCoordinates.y)));
		}

		return nearest;
		
	}
	
	@Override
	public Hit intersect(Ray ray) {
		return BVHRoot.intersect(ray);
	}
	
	@Override
	public Sphere getBoundingSphere() {
		return BVHRoot.toSphere();
	}
	
}
