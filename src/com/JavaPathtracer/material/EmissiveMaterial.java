package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Vector;

public class EmissiveMaterial implements IMaterial {

	private ISampleable emission;

	public EmissiveMaterial(ISampleable emission) {
		this.emission = emission;
	}

	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Pathtracer pathtracer) {
		return emission.sample(hit.textureCoordinates.x, hit.textureCoordinates.y);
	}

}
