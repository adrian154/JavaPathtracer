package com.JavaPathtracer.tonemapping;

import com.JavaPathtracer.geometry.Vector;

public class LinearTonemapper implements IToneMapper {

	@Override
	public Vector map(Vector inColor) {
		return inColor;
	}

}
