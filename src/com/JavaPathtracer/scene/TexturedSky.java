package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;

public class TexturedSky implements Environment {

	protected Sampleable emission;
	
	public TexturedSky(Sampleable emission) {
		this.emission = emission;
	}
	
	@Override
	public Vector getEmission(Vector direction) {
		Vector invDir = new Vector(0.0, 0.0, 0.0).minus(direction);
		double azimuth = Math.atan2(invDir.z, invDir.x);
		double inclination = Math.asin(invDir.y);
		double u = 0.5 + azimuth / (2 * Math.PI);
		double v = 0.5 - inclination / Math.PI;
		return emission.sample(new Vector(u, v));
	}
	
}
