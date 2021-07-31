package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.BRDFMaterial;
import com.JavaPathtracer.scene.Scene;

public class DebugTracer extends Raytracer {

	public enum Mode {
		ALBEDO,
		SIMPLE_SHADED,
		NORMAL,
		SHADED_NORMAL,
		DEPTH,
		UV,
		STENCIL,
		TEST
	}
	
	private Mode mode;

	public DebugTracer(Mode mode) {
		this.mode = mode;
	}
	
	private Vector shadeVector(Vector hit) {
		return hit.plus(Vector.ONE).idiv(2);
	}
	
	private Vector shadeScalar(double depth) {
		return Vector.ONE.divBy(depth + 0.1);
	}
	
	// --- shaders
	private Vector shadeAlbedo(Hit hit, Ray ray) {
		if(hit.material instanceof BRDFMaterial) {
			return ((BRDFMaterial)hit.material).getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
		}
		return new Vector(1.0, 0.0, 1.0);
	}
	
	private Vector shadeSimple(Hit hit, Ray ray) {
		if(hit.material instanceof BRDFMaterial) {
			double shading = Vector.ZERO.minus(ray.direction).dot(hit.normal);
			return shading < 0 ? Vector.ZERO : ((BRDFMaterial)hit.material).getColor(hit.textureCoordinates.x, hit.textureCoordinates.y).times(shading);
		}
		return new Vector(1.0, 0.0, 1.0);
	}
	
	private Vector shadedNormal(Hit hit, Ray ray) {
		return shadeVector(hit.normal).times(Math.max(0, ray.direction.times(-1).dot(hit.normal)));
	}
	
	private Vector shadeNormal(Hit hit, Ray ray) {
		return shadeVector(hit.normal);
	}
	
	private Vector shadeDepth(Hit hit, Ray ray) {
		return shadeScalar(hit.distance);
	}
	
	private Vector shadeUV(Hit hit, Ray ray) {
		return hit.textureCoordinates;
	}
	
	private Vector shadeTest(Hit hit, Ray ray) {
		return shadeVector(hit.normal.getOrthagonal().cross(hit.normal));
	}
	
	private Vector shadeStencil(Hit hit, Ray ray) {
		return hit != null ? Vector.ONE : Vector.ZERO;
	}
	
	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		
		super.traceRay(scene, ray);

		Hit hit = scene.traceRay(ray);
		if (hit != null) {
			switch(mode) {
				case ALBEDO: return shadeAlbedo(hit, ray);
				case SIMPLE_SHADED: return shadeSimple(hit, ray);
				case NORMAL: return shadeNormal(hit, ray);
				case SHADED_NORMAL: return shadedNormal(hit, ray);
				case DEPTH: return shadeDepth(hit, ray);
				case TEST: return shadeTest(hit, ray);
				case STENCIL: return shadeStencil(hit, ray);
				case UV: default: return shadeUV(hit, ray);
			}
		} else {
			return scene.getSkyEmission(ray.direction, true);
		}

	}
	
	@Override
	public String toString() {
		return String.format("Debug raytracer (%s mode)", this.mode.toString());
	}

}
