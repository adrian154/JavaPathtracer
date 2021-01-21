package com.JavaPathtracer.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Raytracer;

// Don't use naked meshes, use BVHMesh!
// This class has a few glaring flaws that make it unusable even if you forcibly trace rays against every poly
public class Mesh {

	public int[] faces;
	public int[] faceNormIndices;
	public int[] texCoordIndices;
	public Vector[] vertexes;
	public Vector[] vertexNormals;
	public Vector[] textureCoordinates;
	public boolean hasTextureData;

	public Mesh(File file, Matrix matrix) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line;
		int lineNum = 1;

		List<Vector> vertexes = new ArrayList<Vector>();
		List<Vector> vertexNormals = new ArrayList<Vector>();
		List<Vector> textureCoordinates = new ArrayList<Vector>();
		List<Integer> faces = new ArrayList<Integer>();
		List<Integer> faceNormIndices = new ArrayList<Integer>();
		List<Integer> texCoordIndices = new ArrayList<Integer>();

		while ((line = reader.readLine()) != null) {

			// Ignore empty lines and comments
			if (line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}

			// Split line
			String[] parts = line.split("\\s+");

			if (parts[0].equals("v")) {

				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("File \"" + file.getName() + "\", line " + lineNum
							+ ": wrong number of components for vertex (expected 3)");
				}

				vertexes.add(matrix.transform(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),
						Double.parseDouble(parts[3]))));

			} else if(parts[0].equals("vn")) {
				
				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("File \"" + file.getName() + "\", line " + lineNum
							+ ": wrong number of components for vertex normal (expected 3)");
				}

				vertexNormals.add(matrix.transform(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]),
						Double.parseDouble(parts[3]))));

				
			} else if (parts[0].equals("f")) {

				int components = parts.length - 1;
				int[] faceVertexes = new int[components];
				int[] faceTexCoords = new int[components];
				int[] faceVertNormals = new int[components];
				
				for(int i = 0; i < parts.length - 1; i++) {
					String[] split = parts[i + 1].split("/");
					faceVertexes[i] = Integer.parseInt(split[0]) - 1;
					if(split.length > 1) faceTexCoords[i] = Integer.parseInt(split[1]) - 1;
					if(split.length > 2) faceVertNormals[i] = Integer.parseInt(split[2]) - 1;
				}
				
				for(int i = 1; i < components - 1; i++) {
					
					faces.add(faceVertexes[0]);
					faces.add(faceVertexes[i]);
					faces.add(faceVertexes[i + 1]);
					
					texCoordIndices.add(faceTexCoords[0]);
					texCoordIndices.add(faceTexCoords[i]);
					texCoordIndices.add(faceTexCoords[i + 1]);
					
					faceNormIndices.add(faceVertNormals[0]);
					faceNormIndices.add(faceVertNormals[i]);
					faceNormIndices.add(faceVertNormals[i + 1]);
					
				}

			} else if (parts[0].equals("vt")) {

				hasTextureData = true;

				/*
				 * if(parts.length != 3) { reader.close(); throw new RuntimeException("File \""
				 * + file.getName() + "\", line " + lineNum +
				 * ": wrong number of components for texture coordinate (expected 2)"); }
				 */

				textureCoordinates.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), 0.0));

			}

			lineNum++;

		}

		// Convert arraylists to arrays
		this.vertexes = vertexes.toArray(new Vector[0]);
		this.vertexNormals = vertexNormals.size() > 0 ? vertexNormals.toArray(new Vector[0]) : null;
		this.faces = faces.stream().mapToInt(Integer::valueOf).toArray();
		this.faceNormIndices = faceNormIndices.stream().mapToInt(Integer::valueOf).toArray();
		this.texCoordIndices = texCoordIndices.stream().mapToInt(Integer::valueOf).toArray();
		this.textureCoordinates = textureCoordinates.toArray(new Vector[0]);

		System.out.println("Mesh statistics: Vertexes=" + vertexes.size() + ", faces=" + faces.size()
				+ ", texCoordIdxes=" + texCoordIndices.size() + ", texCoords=" + textureCoordinates.size());

		reader.close();

	}

	// Moller-Trumbore triangle intersection algorithm
	// Uses barycentric coordinates to test triangle intersection
	// (...as well as some general math trickery)
	// Normals are not normalized. This is to reduce the number of square roots.
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
				if (normal.dot(ray.direction) > 0) {
					normal.invert();
				}
				
			} else {

				Vector norm1 = vertexNormals[faceNormIndices[which * 3]];
				Vector norm2 = vertexNormals[faceNormIndices[which * 3 + 1]];
				Vector norm3 = vertexNormals[faceNormIndices[which * 3 + 2]];
				normal = norm1.times(1 - u - v).plus(norm2.times(u)).plus(norm3.times(v)).normalize();
				
			}
			
			return new Hit(ray.getPoint(t), normal, t, new Vector(u, v, 0.0));
		} else {
			return Hit.MISS;
		}

	}

	public Hit intersect(Ray ray, int[] prims) {

		Hit nearest = Hit.MISS;
		int nearestIndex = 0;
		for (int i : prims) {

			Hit cur = intersectTri(ray, i);
			if (cur.distance < nearest.distance) {
				nearest = cur;
				nearestIndex = i;
			}

		}

		if (nearest.hit && hasTextureData) {
			Vector tex1 = textureCoordinates[texCoordIndices[nearestIndex * 3]];
			Vector tex2 = textureCoordinates[texCoordIndices[nearestIndex * 3 + 1]];
			Vector tex3 = textureCoordinates[texCoordIndices[nearestIndex * 3 + 2]];
			nearest.textureCoordinates = tex1.plus((tex2.minus(tex1).times(nearest.textureCoordinates.x))
					.plus(tex3.minus(tex1).times(nearest.textureCoordinates.y)));
		}

		return nearest;

	}
	
}
