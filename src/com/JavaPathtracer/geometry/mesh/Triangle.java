package com.JavaPathtracer.geometry.mesh;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Transform;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.WorldObject;

public class Triangle extends WorldObject {

	public Vector vert0, vert1, vert2;
	public Vector norm0, norm1, norm2;
	public Vector texCoord0, texCoord1, texCoord2;
	public String materialName;
	
	public Triangle(Transform transform) {
		super(transform);
	}
	
	// use Moller-Trombore algorithm to calculate ray-triangle intersection
	// the barycentric coordinates of the hit are returned as texture coordinates for convenience
	private Hit intersectTri(Ray ray, int face) {
		
		Vector edge1 = vert1.minus(vert0);
		Vector edge2 = vert2.minus(vert0);
		Vector h = ray.direction.cross(edge2);

		double a = edge1.dot(h);
		if (a > -Pathtracer.EPSILON && a < Pathtracer.EPSILON) {
			return Hit.MISS;
		}

		double f = 1 / a;
		Vector s = ray.origin.minus(vert0);
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
			
			if(norm0 == null) {
			
				// no vertex normals so can't interpolate :(
				// treat triangle as flat
				normal = edge1.cross(edge2);
				
			} else {

				// interpolate between vertex normals based on barycentric coordinates
				normal = norm0.times(1 - u - v).plus(norm1.times(u)).plus(norm2.times(v));
				
			}
		
			// TODO: tangent vector
			normal = normal.normalize().facing(ray.direction);
			return new Hit(ray, ray.getPoint(t), normal, null, t, new Vector(u, v, 0.0));
			
		} else {
			return Hit.MISS;
		}

	}
	
	
	@Override
	public ObjectHit raytraceLocal(Ray ray) {
		
		
		
	}
	
}
