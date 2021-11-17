package com.JavaPathtracer.tonemapping;

import com.JavaPathtracer.geometry.Vector;

// Uses Krzysztof Narkowicz's approximate ACES curve
// See https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/
public class ACESTonemapper implements IToneMapper {
	
	private boolean independent; // whether the func is applied to each channel separately
	private static final double A = 2.51;
	private static final double B = 0.03;
	private static final double C = 2.43;
	private static final double D = 0.59;
	private static final double E = 0.14;
	
	public ACESTonemapper() {
		this(true);
	}
	
	public ACESTonemapper(boolean independent) {
		this.independent = independent;
	}
	
	private double func(double in) {
		double v = in * 0.6;
		return v * (A * v + B) / (v * (C * v + D) + E);
	}
	
	@Override
	public Vector map(Vector in) {
		/*
		double x = in.x * 0.6;
		double y = in.y * 0.6;
		double z = in.z * 0.6;
		return new Vector(
			x * (A * x + B) / (x * (C * x + D) + E),
			y * (A * y + B) / (y * (C * y + D) + E),
			z * (A * z + B) / (z * (C * z + D) + E)
		);
		*/
		// TODO: fix fucked up independent mapping
		double r = func(in.x), g = func(in.y), b = func(in.z);
		if(independent) {
			return new Vector(r, g, b);
		} else {
			double max = Math.max(Math.max(r, g), b);
			return new Vector(r / max, g / max, b / max);
		}
	}
	
	@Override
	public String toString() {
		return "Approximate ACES";
	}
	
}
