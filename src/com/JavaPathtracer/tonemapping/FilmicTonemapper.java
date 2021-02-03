package com.JavaPathtracer.tonemapping;

import com.JavaPathtracer.geometry.Vector;

public class FilmicTonemapper implements IToneMapper {

	public double hable(double x) {

		double A = 0.15;
		double B = 0.5;
		double C = 0.1;
		double D = 0.2;
		double E = 0.02;
		double F = 0.3;

		return ((x * (A * x + C * B) + D * E) / (x * (A * x + B) + D * F)) - E / F;

	}

	@Override
	public Vector map(Vector inColor) {
		double max = Math.max(inColor.x, Math.max(inColor.y, inColor.z));
		return inColor.times(hable(max) / max);
	}
	
	@Override
	public String toString() {
		return "Filmic";
	}

}
