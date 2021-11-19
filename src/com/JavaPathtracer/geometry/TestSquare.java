package com.JavaPathtracer.geometry;

import com.JavaPathtracer.pattern.Sampleable;

public class TestSquare extends Square {

	private Sampleable normalMap;
	
	public TestSquare(double size, Sampleable normalMap) {
		super(Vector.Y, new Vector(0, 87, 0), size, size, true);
		this.normalMap = normalMap;
	}
	
	@Override
	public Hit raytrace(Ray ray) {
		
		Hit hit = super.raytrace(ray);
		if(hit != null) {
			Vector color = normalMap.sample(hit.textureCoordinates.x, hit.textureCoordinates.y);
			hit.normal = new Vector((color.x - 0.5) * 2, (color.z - 0.5) * 2, (color.y - 0.5) * 2);
		}
		
		return hit;
		
	}
	
}
