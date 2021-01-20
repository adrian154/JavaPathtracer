package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Sampleable;

public class TexturedSky {

	protected Sampleable emission;
	
	public TexturedSky(Sampleable emission) {
		this.emission = emission;
	}
	
	public Vector getEmission(Vector direction) {
		Vector invDir = new Vector(0.0, 0.0, 0.0).minus(direction);
		double azimuth = (Math.atan2(invDir.z, invDir.x) + 0.5) % (2 * Math.PI);
		double inclination = Math.asin(invDir.y);
		double u = 0.5 + azimuth / (2 * Math.PI);
		double v = 0.5 - inclination / Math.PI;
		return emission.sample(u, v);
	}
	
}
