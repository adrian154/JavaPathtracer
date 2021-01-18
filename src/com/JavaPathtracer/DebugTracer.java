package com.JavaPathtracer;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.BaseMaterial;
import com.JavaPathtracer.material.IMaterial;

public class DebugTracer extends Raytracer {

	@Override
	public Vector traceRay(Scene scene, Ray ray) {

		Hit hit = scene.traceRay(ray);
		if (hit.hit) {

			double amt = ray.origin.minus(hit.point).normalized().dot(hit.normal);
			IMaterial material = hit.hitObject.getMaterial();

			Vector color;
			if (material instanceof BaseMaterial) {
				BaseMaterial baseMat = (BaseMaterial) material;
				color = baseMat.getColor(hit.textureCoordinates.x, hit.textureCoordinates.y);
			} else {
				color = new Vector(1.0, 0.0, 1.0);
			}

			return color.times(amt < 0 ? 0 : amt);

		} else {
			return scene.getSkyEmission(ray.direction);
		}

	}

}
