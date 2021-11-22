package com.JavaPathtracer.testscenes;

import java.io.IOException;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.FresnelMixer;
import com.JavaPathtracer.material.GlassMaterial;
import com.JavaPathtracer.material.MirrorMaterial;
import com.JavaPathtracer.pattern.Checkerboard;
import com.JavaPathtracer.pattern.HDRMap;
import com.JavaPathtracer.scene.Scene;
import com.JavaPathtracer.scene.TexturedSky;

public class MaterialsTest extends Scene {
	
	public Camera getCamera() {
		Camera camera = new PerspectiveCamera();
		camera.moveTo(new Vector(8, 1, 10));
		camera.lookAt(new Vector(0, 0, -1));
		return camera;
	}
	
	public MaterialsTest() throws IOException {
		
		this.setSky(new TexturedSky(new HDRMap("assets/hallway.hdr")));
		
		// floor
		this.add(new Plane(new Vector(0, 1, 0), new Vector(0, 0, 0), 2), new DiffuseMaterial(new Checkerboard()));
		
		int i = 0;
		double dist = 2.5;
		this.add(new Sphere(new Vector(i * dist, 1.0, 5), 1.0), new DiffuseMaterial(new Vector(0x00ffff)));
		
		i++;
		this.add(new Sphere(new Vector(i * dist, 1.0, 5), 1.0), new MirrorMaterial(Vector.ONE));
		
		i++;
		this.add(new Sphere(new Vector(i * dist, 1.0, 5), 1.0), new MirrorMaterial(new Vector(0xffd84a)));

		i++;
		this.add(new Sphere(new Vector(i * dist, 1.0, 5), 1.0), new FresnelMixer(
			new DiffuseMaterial(new Vector(0x9842f5)),
			new MirrorMaterial(Vector.ONE),
			0.05
		));
		
		i++;
		this.add(new Sphere(new Vector(i * dist, 1.0, 5), 1.0), new GlassMaterial(1.5));
		
	}
	
}
