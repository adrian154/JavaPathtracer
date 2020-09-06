package com.JavaPathtracer.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Raytracer;

// Don't use naked meshes, use BVHMesh!
public class Mesh {

	public int[] faces;
	public Vector[] vertexes;
	
	public Mesh(File file) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line;
		int lineNum = 1;
		
		List<Vector> vertexes = new ArrayList<Vector>();
		List<Integer> faces = new ArrayList<Integer>();
		
		while((line = reader.readLine()) != null) {
			
			// Ignore empty lines and comments
			if(line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}
			
			// Split line
			String[] parts = line.split("\\s+");
			
			if(parts[0].equals("v")) {
				
				if(parts.length != 4) {
					throw new RuntimeException("File \"" + file.getName() + "\", line " + lineNum + ": wrong number of parameters for vertex (expected 4)");
				}
				
				vertexes.add(new Vector(
					Double.parseDouble(parts[1]),
					Double.parseDouble(parts[2]),
					Double.parseDouble(parts[3])
				));
				
			} else if(parts[0].equals("f")) {
				
				// This could be extended into a loop for arbitrary-length polygons
				// In practice, few meshes will ever have anything but tris and quads
				if(parts.length == 4) {
					faces.add(Integer.parseInt(parts[1].split("/")[0]) - 1);
					faces.add(Integer.parseInt(parts[2].split("/")[0]) - 1);
					faces.add(Integer.parseInt(parts[3].split("/")[0]) - 1);
				} else if(parts.length == 5) {
					
					// Useless micro-optimization since I really can't help myself
					int first = Integer.parseInt(parts[1].split("/")[0]) - 1;
					int third = Integer.parseInt(parts[3].split("/")[0]) - 1;
					
					faces.add(first);
					faces.add(Integer.parseInt(parts[2].split("/")[0]) - 1);
					faces.add(third);
					
					faces.add(first);
					faces.add(third);
					faces.add(Integer.parseInt(parts[4].split("/")[0]) - 1);
				
				}
				
			}
			
			lineNum++;
			
		}
		
		// Convert arraylists to arrays
		this.vertexes = vertexes.toArray(new Vector[0]);
		this.faces = faces.stream().mapToInt(Integer::valueOf).toArray();
		reader.close();
		
	}
	
	// Moller-Trumbore triangle intersection algorithm
	// Uses barycentric coordinates to test triangle intersection
	// (...as well as some general math trickery)
	public static final Hit intsersectTri(Ray ray, Vector v0, Vector v1, Vector v2) {
		
		Vector edge1 = v1.minus(v0);
		Vector edge2 = v2.minus(v0);
		Vector h = ray.direction.cross(edge2);
		
		double a = edge1.dot(h);
		if(a > -Raytracer.EPSILON && a < Raytracer.EPSILON) {
			return Hit.MISS;
		}
		
		double f = 1 / a;
		Vector s = ray.origin.minus(v0);
		double u = f * s.dot(h);
		if(u < 0.0 || u > 1.0)
			return Hit.MISS;
		
		Vector q = s.cross(edge1);
		double v = f * ray.direction.dot(q);
		if(v < 0.0 || u + v > 1.0)
			return Hit.MISS;
		
		double t = f * edge2.dot(q);
		if(t > Raytracer.EPSILON) {
			
			// Make sure normal faces ray direction
			Vector normal = edge1.cross(edge2).normalized();
			if(normal.dot(ray.direction) < 0) {
				normal.invert();
			}
			
			return new Hit(ray.getPoint(t), normal, t, new Vector(u, v, 0.0));
		} else {
			return Hit.MISS;
		}
		
	}

	/*
	public Hit intersect(Ray ray) {
		
		// Loop through all tris (INCREDIBLY SLOW)
		Hit nearest = Hit.MISS;
		for(int i = 0; i < faces.length / 3; i++) {
			Hit cur = Mesh.intsersectTri(ray, vertexes[faces[i * 3]], vertexes[faces[i * 3 + 1]], vertexes[faces[i * 3 + 2]]);
			if(cur.distance < nearest.distance) nearest = cur;
		}
		
		return nearest;
	
	}
	*/
	
}
