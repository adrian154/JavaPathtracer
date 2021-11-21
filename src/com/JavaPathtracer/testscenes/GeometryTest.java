package com.JavaPathtracer.testscenes;

import java.io.IOException;
import java.util.Map;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Transform;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.mesh.BVHNode;
import com.JavaPathtracer.geometry.mesh.Mesh;
import com.JavaPathtracer.geometry.mesh.OBJLoader;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.pattern.Texture;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.SimpleSky;

public class GeometryTest extends Scene {
	
	public Camera getCamera() {
		Camera camera = new PerspectiveCamera();
		camera.moveTo(new Vector(8, 8, 16));
		camera.lookAt(new Vector(0, -1, -3).normalize());
		return camera;
	}
	
	public GeometryTest() throws IOException {
		
		this.setSky(new SimpleSky(Vector.ONE.times(10)));
		
		// floor
		this.add(new Plane(new Vector(0, 1, 0), new Vector(0, 0, 0)), new DiffuseMaterial(Vector.ONE));
		
		// ball
		//this.add(new Sphere(new Vector(0, 1.0, 5), 1.0), new DiffuseMaterial(new Vector(0x00ffff)));
		
		// mesh
		BVHNode africanHead = new BVHNode(OBJLoader.load("assets/AfricanHead.obj"));
		Map<String, Material> materials = Map.of("", new DiffuseMaterial(new Texture("assets/AfricanHead.png")));
		
		for(int x = 0; x < 8; x++) {
			for(int z = 0; z < 8; z++) {
				this.add(new Mesh(africanHead, new Transform().translate(x * 2, 5, z * 2).complete(), materials));
			}
		}

	}
	
}
