package com.JavaPathtracer.debug;

import com.JavaPathtracer.Raytracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.BRDFMaterial;
import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.scene.Scene;

public class DebugTracer extends Raytracer {

	public enum Mode {
		ALBEDO,
		SIMPLE_SHADED,
		NORMAL,
		DEPTH,
		UV,
		SCATTER
	}
	
	private Mode mode;
	private BRDFMaterial material;
	
	public DebugTracer(Mode mode) {
		this.mode = mode;
	}
	
	public DebugTracer(Mode mode, BRDFMaterial material) {
		if(mode != Mode.SCATTER) throw new RuntimeException("Can't instantiate DebugTracer with material outside of scattering mode");
		this.mode = mode;
		this.material = material;
	}
	
	private Vector shadeVector(Vector hit) {
		return hit.plus(Vector.ONE).idiv(2);
	}
	
	private Vector shadeScalar(double depth) {
		return new Vector(50).divBy(depth + 0.1);
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
	
	private Vector shadeNormal(Hit hit, Ray ray) {
		return shadeVector(hit.normal);
	}
	
	private Vector shadeDepth(Hit hit, Ray ray) {
		return shadeScalar(hit.distance);
	}
	
	private Vector shadeUV(Hit hit, Ray ray) {
		return hit.textureCoordinates;
	}
	
	private Vector shadeScatterVector(Hit hit, Ray ray) {
		return shadeVector(material.sample(ray.direction, hit));
	}
	
	@Override
	public Vector traceRay(Scene scene, Ray ray) {
		
		super.traceRay(scene, ray);

		Hit hit = scene.traceRay(ray);
		if (hit.hit) {

			switch(mode) {
				case ALBEDO: return shadeAlbedo(hit, ray);
				case SIMPLE_SHADED: return shadeSimple(hit, ray);
				case NORMAL: return shadeNormal(hit, ray);
				case DEPTH: return shadeDepth(hit, ray);
				case SCATTER: return shadeScatterVector(hit, ray);
				case UV: default: return shadeUV(hit, ray);
			}
			
		} else {
			return scene.getSkyEmission(ray.direction);
		}

	}

}
