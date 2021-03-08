package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.Scene;

public class GlassMaterial implements Material {

	public double IOR;
	
	public GlassMaterial(double IOR) {
		this.IOR = IOR;
	}
	
	public Vector shade(Hit hit, int bounces, Scene scene, Pathtracer pathtracer, double iorIn) {
		
		// see GLSL's refract() for explanation on the following terms
		// basically, a clever application of Snell's formula avoiding pesky trig funcs
		// I would not have been smart enough to figure this one out on my own :(
		Vector normal = hit.normal;
		Vector incident = hit.ray.direction;
		double NdotI = normal.dot(incident);
		double eta = iorIn / IOR;
		double k = 1.0 - eta * eta * (1.0 - NdotI * NdotI);
		
		// fresnel calculations
		// F0 = reflection at normal incidence (looking straight on)
		// take advantage of the fact that everything is normalized -> swap cosines with dot products
		double F0 = (eta - 1) * (eta - 1) / ((eta + 1) * (eta + 1));
		double a = 1 + NdotI;
		double reflectance = F0 + (1 - F0) * a*a*a*a*a;
		Vector reflectVector = MirrorMaterial.reflect(normal, incident);
		
		// k < 0: TIR case
		if(k < 0) {
		
			Vector reflect = MirrorMaterial.reflect(normal, incident);
			Ray next = new Ray(hit.point, reflect);
			return pathtracer.pathtraceRay(scene, next, bounces + 1, true, IOR);
		
		} else {
			
			// good ol refraction
			Vector refracted = incident.times(eta).minus(normal.times(eta * NdotI + Math.sqrt(k)));
			Ray refractRay = new Ray(hit.point, refracted);
			Vector refract = pathtracer.pathtraceRay(scene, refractRay, bounces + 1, true, IOR);
		
			Ray reflectRay = new Ray(hit.point, reflectVector);
			Vector reflect = pathtracer.pathtraceRay(scene, reflectRay, bounces + 1, true, iorIn);
			
			return reflect.times(reflectance).plus(refract.times(1 - reflectance));
			
		}
		
	}
	
	@Override
	public String toString() {
		return String.format("Glass");
	}

	
}
