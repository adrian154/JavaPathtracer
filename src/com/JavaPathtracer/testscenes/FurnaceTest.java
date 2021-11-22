package com.JavaPathtracer.testscenes;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.SimpleSky;

// used to check a BRDF for energy conservation
// the sphere should perfectly blend into the environment
public class FurnaceTest extends Scene {

	public Material createMaterial() {
		return new DiffuseMaterial(Vector.ONE);
	}
	
	public Camera createCamera() {
		Camera camera = new PerspectiveCamera();
		camera.moveTo(new Vector(0, 0, -5));
		return camera;
	}
	
	public FurnaceTest() {
		
		this.setSky(new SimpleSky(Vector.ONE));
		this.add(new Sphere(Vector.ZERO, 1), this.createMaterial());
		
	}
	
}
