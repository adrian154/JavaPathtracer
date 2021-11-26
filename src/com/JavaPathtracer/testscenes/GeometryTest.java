package com.JavaPathtracer.testscenes;

import java.io.IOException;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Circle;
import com.JavaPathtracer.geometry.Cylinder;
import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Square;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.pattern.Checkerboard;
import com.JavaPathtracer.scene.Scene;

public class GeometryTest extends Scene {
	
	private static final Material mtl = new DiffuseMaterial(Vector.ONE);
	
	private void addCylinder(Vector position, Vector direction, double radius, double length) {
		this.add(new Cylinder(position, direction, radius, length), mtl);
		this.add(new Circle(direction, position, radius), mtl);
		this.add(new Circle(direction, position.plus(direction.times(length)), radius), mtl);
	}
	
	public GeometryTest() throws IOException {
		
		this.add(new Square(new Vector(0, 1, 0), new Vector(0, -3, 0), 15), new DiffuseMaterial(new Checkerboard()));
		
		this.add(new BoundingBox(new Vector(1, 1, 1), new Vector(2, 2, 2)), mtl);
		this.add(new Sphere(new Vector(3, 2, 0), 1.0), mtl);
				
		this.addCylinder(new Vector(5, 0, 0), Vector.Y, 1.5, 3);
		this.addCylinder(new Vector(0, 0, -5), new Vector(-10, 3, 6).normalize(), 1.5, 3);

	}
	
}
