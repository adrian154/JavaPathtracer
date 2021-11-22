package com.JavaPathtracer.geometry.mesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.JavaPathtracer.geometry.Transform;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;

public class OBJLoader {
	
	public static Mesh load(String path, Map<String, Material> materials, Transform transform) throws IOException {
		return new Mesh(new BVHNode(OBJLoader.parse(path)), transform, materials);
	}
	
	public static MeshGeometry parse(String path) throws IOException {
		return OBJLoader.parse(new File(path));
	}

	// If the model references a material that is not supplied in the `materials` argument, "default" is used
	public static MeshGeometry parse(File file) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lineNum = 0;

		List<Integer> faces = new ArrayList<Integer>();
		
		// per vertex arrays
		List<Vector> vertexes = new ArrayList<>();
		List<Vector> vertexNormals = new ArrayList<>();
		List<Vector> textureCoordinates = new ArrayList<>();
		List<Integer> normalIndices = new ArrayList<>();
		List<Integer> texCoordIndices = new ArrayList<>();
		
		// per face arrays
		List<String> materialIDs = new ArrayList<>();
		
		// iterate over lines of the model
		String line, currentMaterial = "";
		
		while ((line = reader.readLine()) != null) {

			lineNum++;
			
			// ignore empty lines and comments
			if (line.length() == 0 || line.charAt(0) == '#') {
				continue;
			}

			// Split line
			String[] parts = line.split("\\s+");

			if(parts[0].equals("usemtl")) {
				
				if(parts.length != 2) {
					reader.close();
					throw new RuntimeException("Incomplete `usemtl` directive.");
				}
				
				currentMaterial = parts[1].intern();
				
			} else if (parts[0].equals("v")) {

				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("Unexpected vertex with number of components unequal to 3 at line " + lineNum);
				}

				vertexes.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));

			} else if(parts[0].equals("vn")) {
				
				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("Unexpected vertex normal with number of components unequal to 3 at line " + lineNum);
				}

				vertexNormals.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
				
			} else if (parts[0].equals("f")) {

				int components = parts.length - 1;
				if(components < 3) {
					reader.close();
					throw new RuntimeException("Unexpected polygon with fewer than three vertexes at line " + lineNum);
				}
				
				// collect vertex, texture coordinate, and normal indices and add to the list
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
					
					normalIndices.add(faceVertNormals[0]);
					normalIndices.add(faceVertNormals[i]);
					normalIndices.add(faceVertNormals[i + 1]);
					
					materialIDs.add(currentMaterial);
					
				}

			} else if (parts[0].equals("vt")) {
				
				if(parts.length < 2) {
					reader.close();
					throw new RuntimeException("Unexpected texture coordinate with fewer than three components at line " + lineNum);
				}
				
				textureCoordinates.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), 0.0));
			
			}

		}
		
		reader.close();
		
		// Convert arraylists to arrays
		return new MeshGeometry(
			vertexes,
			vertexNormals,
			textureCoordinates,
			faces,
			normalIndices,
			texCoordIndices,
			materialIDs
		);
		
	}
	
}
