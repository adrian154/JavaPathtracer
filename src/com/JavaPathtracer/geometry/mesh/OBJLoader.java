package com.JavaPathtracer.geometry.mesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;

public class OBJLoader {
	
	public static Mesh load(String path, Map<String, Material> materials) throws IOException {
		return OBJLoader.load(new File(path), materials);
	}

	// If the model references a material that is not supplied in the `materials` argument, "default" is used
	public static Mesh load(File file, Map<String, Material> materials) throws IOException {
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lineNum = 0;

		List<Integer> faces = new ArrayList<Integer>();
		
		// arrays indexed by vertex
		List<Vector> vertexes = new ArrayList<>();
		List<Vector> vertexNormals = new ArrayList<>();
		List<Vector> textureCoordinates = new ArrayList<>();
		List<Integer> normalIndices = new ArrayList<>();
		List<Integer> texCoordIndices = new ArrayList<>();
		
		// arrays indexed by face
		List<Material> materialsList = new ArrayList<>();
		
		// iterate over lines of the model
		String line;
		Material currentMaterial;
		
		while ((line = reader.readLine().trim()) != null) {

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
				
				if((currentMaterial = materials.get(parts[1])) == null) {
					if((currentMaterial = materials.get("default")) == null) {
						reader.close();
						throw new RuntimeException("No material supplied for " + parts[1]);
					} else {
						System.out.println("warning: using default material for " + parts[1]);
					}
				}
				
			} else if (parts[0].equals("v")) {

				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("Unexpected vertex with number of components unequal to 3 at line " + line);
				}

				vertexes.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));

			} else if(parts[0].equals("vn")) {
				
				if (parts.length != 4) {
					reader.close();
					throw new RuntimeException("Unexpected vertex normal with number of components unequal to 3 at line " + line);
				}

				vertexNormals.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3])));
				
			} else if (parts[0].equals("f")) {

				int components = parts.length - 1;
				if(components < 3) {
					throw new RuntimeException("Unexpected polygon with fewer than three vertexes at line " + line);
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
					
				}

			} else if (parts[0].equals("vt")) {
				textureCoordinates.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), 0.0));
			}

		}
		
		reader.close();
		
		// Convert arraylists to arrays
		return new Mesh(
			vertexes,
			vertexNormals,
			textureCoordinates,
			faces,
			normalIndices,
			texCoordIndices,
			materialsList
		);
		
	}
	
}
