package com.JavaPathtracer.scenes;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.SimpleSky;

public class TestScene extends Scene {

	public TestScene() {
		
		this.setSky(new SimpleSky(Vector.ONE.times(3)));
		
		Material mat = new DiffuseMaterial(new Vector(0xffaaaa));
		this.add(new Plane(Vector.Y, Vector.ZERO), mat);
		this.add(new Sphere(Vector.ONE, 0.5), mat);
		
	}
	
	@Override
	public Camera createCamera() {
		Camera camera = new PerspectiveCamera();
		camera.moveTo(new Vector(3, 3, 3));
		camera.lookAt(camera.getPos().reversed().normalize());
		return camera;
	}
	
}
