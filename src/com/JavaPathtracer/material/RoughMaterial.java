package com.JavaPathtracer.material;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.pattern.Sampleable;
import com.JavaPathtracer.pattern.SampleableScalar;

public class RoughMaterial extends DiffuseMaterial {

	private SampleableScalar roughness;
	
	public RoughMaterial(Sampleable color, SampleableScalar roughness) {
		super(color);
		this.roughness = roughness;
	}
	
	@Override
	public double BRDF(Vector incident, Vector outgoing, Vector normal, Vector textureCoordinates) {
		
		// beware: disgusting variable names
		double ndv = Math.max(normal.dot(incident), 0);
		double incidentTheta = Math.acos(ndv);
		double ndl = Math.max(normal.dot(outgoing), 0);
		double outgoingTheta = Math.acos(ndl);
		
		double sigma = this.roughness.sampleScalar(textureCoordinates.x, textureCoordinates.y);
		double sigmaSquared = sigma * sigma;
		double A = 1 - 0.5 * sigmaSquared / (sigmaSquared + 0.33);
		double B = 0.45 * sigmaSquared / (sigmaSquared + 0.99);
		double alpha = Math.max(incidentTheta, outgoingTheta);
		double beta = Math.min(incidentTheta, outgoingTheta);
		
		return A + B * Math.max(0, Math.cos(incidentTheta - outgoingTheta) * Math.sin(alpha) * Math.tan(beta));
		
	}
	
}
