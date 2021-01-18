package com.JavaPathtracer.material;

import com.JavaPathtracer.Pathtracer;
import com.JavaPathtracer.Scene;
import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;

public class MirrorMaterial extends BaseMaterial {

	public MirrorMaterial(ISampleable color) {
		super(color);
	}

	@Override
	public Vector shade(Vector incident, Hit hit, int bounces, Scene scene, Pathtracer pathtracer) {

		Vector reflect = incident.minus(hit.normal.times(2 * hit.normal.dot(incident)));
		Ray next = new Ray(hit.point, reflect);
		return pathtracer.pathtraceRay(scene, next, bounces + 1)
				.times(this.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y));

	}

}
