package com.JavaPathtracer;

import com.JavaPathtracer.geometry.ObjectHit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public class DebugTracer implements Raytracer {

	public enum Mode {
		ALBEDO,
		SIMPLE_SHADED,
		NORMALS,
		SHADED_NORMALS,
		DEPTH,
		UV
	}
	
	private Mode mode;

	public DebugTracer(Mode mode) {
		this.mode = mode;
	}
	
	// --- shaders
	private Vector shadeAlbedo(ObjectHit hit) {
		return hit.material.getDebugColor(hit.textureCoord);
	}
	
	private Vector shadeSimple(ObjectHit hit) {
		double shading = Vector.ZERO.minus(hit.ray.direction).dot(hit.normal);
		return shading < 0 ? Vector.ZERO : hit.material.getDebugColor(hit.textureCoord).times(shading);		
	}
	
	private Vector shadeNormal(ObjectHit hit) {
		return hit.normal.plus(1).divBy(2);
	}
	
	private Vector shadeNormalFlat(ObjectHit hit) {
		return hit.normal.plus(1).times(hit.ray.direction.reverse().dot(hit.normal) / 2);
	}
	
	private Vector shadeDepth(ObjectHit hit) {
		return new Vector(1 / (hit.distance + 0.1));
	}
	
	private Vector shadeUV(ObjectHit hit) {
		return hit.textureCoord;
	}

	@Override
	public Vector traceRay(Scene scene, Ray ray) {

		ObjectHit hit = scene.traceRay(ray, false);
		if (hit.hit) {
			switch(mode) {
				case ALBEDO: return shadeAlbedo(hit);
				case SIMPLE_SHADED: return shadeSimple(hit);
				case NORMALS: return shadeNormal(hit);
				case SHADED_NORMALS: return shadeNormalFlat(hit);
				case DEPTH: return shadeDepth(hit);
				case UV: default: return shadeUV(hit);
			}
		} else {
			return scene.getSky().getEmission(ray.direction);
		}

	}
	
	@Override
	public String toString() {
		return String.format("Debug raytracer (%s mode)", this.mode.toString());
	}

}
