package com.JavaPathtracer.geometry.mesh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.JavaPathtracer.Stopwatch;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Matrix4x4;
import com.JavaPathtracer.geometry.MeshHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.WorldObject;

// Don't use naked meshes, use BVHMesh!
// This class has a few glaring flaws that make it unusable even if you forcibly trace rays against every poly
public class MeshObject implements WorldObject {

	protected MeshGeometryContainer geometry;
	protected List<Material> materials;
	protected int[] faceMaterials;
	
	public MeshObject(String string, Map<String, Material> materials) throws IOException {
		this(new File(string), new Matrix4x4(), new Matrix4x4(), materials);
	}
	
	public MeshObject(File file, Matrix4x4 matrix, Map<String, Material> materials) throws IOException {
		this(file, matrix, matrix, materials);
	}
	
	public MeshObject(File file, Matrix4x4 matrix, Matrix4x4 normTransform, Map<String, Material> materials) throws IOException {

		// set locals
		this.materials = materials.values().stream().collect(Collectors.toList());
		if(materials.size() == 0) {
			throw new RuntimeException("No materials were provided");
		}
		
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
		List<Integer> faceMaterialIndices = new ArrayList<Integer>();
		int currentMaterialIdx = 0;
		
		while ((line = reader.readLine()) != null) {

			// Ignore empty lines and comments
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
				
				Material material = materials.get(parts[1]);
				if(material == null) {
					System.out.println("Warning: using default material since no material provided for \"" + parts[1] + "\"");
					currentMaterialIdx = 0;
				} else {
					currentMaterialIdx = this.materials.indexOf(material);
				}
				
			} else if (parts[0].equals("v")) {

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

					faceMaterialIndices.add(currentMaterialIdx);
					
				}

			} else if (parts[0].equals("vt")) {
				textureCoordinates.add(new Vector(Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), 0.0));
			}

			lineNum++;

		}

		stopwatch.lap("parseFile");
		
		// Convert arraylists to arrays
		this.geometry = new MeshGeometryContainer(
			vertexes,
			vertexNormals,
			textureCoordinates,
			faces,
			faceNormIndices,
			texCoordIndices
		);
		
		stopwatch.lap("constructGeomContainer");
		
		faceMaterials = faceMaterialIndices.stream().mapToInt(Integer::valueOf).toArray();
		reader.close();
		stopwatch.stop();

	}
	
	@Override
	public Hit traceRay(Ray ray) {
		
		Hit hit = geometry.intersect(ray);
		
		if(hit != null) {
			MeshHit meshHit = (MeshHit)hit;
			hit.material = materials.get(faceMaterials[meshHit.face]);
			return hit;
		}
		
		return hit;
		
	}

}
