package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

public class DayNightSky implements Environment {

	protected Vector color1, color2;
	
	public DayNightSky(Vector color1, Vector color2) {
		this.color1 = color1;
		this.color2 = color2;
	}
	
	public Vector getEmission(Vector direction) {
		double factor = (direction.y + 1) / 2;
		return factor > 0.5 ? color1 : color2;
		
	}
	
}
