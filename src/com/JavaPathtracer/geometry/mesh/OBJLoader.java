package com.JavaPathtracer.geometry.mesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.JavaPathtracer.Stopwatch;
import com.JavaPathtracer.geometry.Matrix4x4;
import com.JavaPathtracer.geometry.Vector;

// Don't use naked meshes, use BVHMesh!
// This class has a few glaring flaws that make it unusable even if you forcibly trace rays against every poly
public class OBJLoader {
	
	public static Mesh load(String path) throws IOException {
		return load(new File(path), new Matrix4x4(), new Matrix4x4());
	}
	
	public static Mesh load(File file, Matrix4x4 matrix) throws IOException {
		return load(file, matrix, matrix);
	}
	
	public static Mesh load(File file, Matrix4x4 matrix, Matrix4x4 normTransform) throws IOException {

		// read mesh
		Stopwatch stopwatch = new Stopwatch("LoadMesh");
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
					throw new RuntimeException("File \"" + file.getName() + "\", line " + lineNum + ": wrong number of components for vertex (expected 3)");
				}

				vertexes.add(matrix.transform(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]))));

			} else if(parts[0].equals("vn")) {
				
				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("File \"" + file.getName() + "\", line " + lineNum + ": wrong number of components for vertex normal (expected 3)");
				}

				vertexNormals.add(normTransform.transform(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]))));
				
			} else if (parts[0].equals("f")) {

				int components = parts.length - 1;
				int[] faceVertexes = new int[components];
				int[] faceTexCoords = new int[components];
				int[] faceVertNormals = new int[components];
				
				for(int i = 0; i < parts.length - 1; i++) {
					String[] split = parts[i + 1].split("/");
					faceVertexes[i] = Integer.parseInt(split[0]) - 1;
					if(split.length > 1 && split[1].length() > 0) faceTexCoords[i] = Integer.parseInt(split[1]) - 1;
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
				textureCoordinates.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), 0.0));
			}

			lineNum++;

		}

		stopwatch.lap("parseFile");
		reader.close();
		stopwatch.stop();
		
		// Convert arraylists to arrays
		return new Mesh(
			vertexes,
			vertexNormals,
			textureCoordinates,
			faces,
			faceNormIndices,
			texCoordIndices
		);
		
	}

}
