package com.JavaPathtracer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.OrthoCamera;
import com.JavaPathtracer.geometry.Matrix4x4;
import com.JavaPathtracer.geometry.Square;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.geometry.mesh.MeshObject;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.SimpleSky;

public class StatueScene extends Scene {

	@Override
	public Camera createCamera() {
		OrthoCamera camera = new OrthoCamera();
		camera.moveTo(new Vector(1.73/2, -1.8, -3));
		camera.lookAt(new Vector(0, 0, 3).normalize());
		camera.setScale(2.0);
		return camera;
	}
	
	public StatueScene() throws IOException {
		
		this.setSky(new SimpleSky(new Vector(0x282f57).divBy(3)));

		this.add(new Square(new Vector(0, -1, 0), new Vector(1.73/2, 0.5, 1.78/2), 1.5), new EmissiveMaterial(new Vector(0xfffb94).times(30)));
		long start = System.currentTimeMillis();
		
		Matrix4x4 mat = new Matrix4x4(new double[] {
			1, 0, 0, 0,
			0, 0, 1, 0,
			0, 1, 0, 0,
			0, 0, 0, 1
		});
		
		this.add(new MeshObject(new File("assets/statue.obj"), mat, mat, Map.of(
			"default", new DiffuseMaterial(Vector.ONE)
		)));

		
	}
	
}
