package com.JavaPathtracer.tonemapping;

import com.JavaPathtracer.geometry.Vector;

public class InverseTonemapper implements IToneMapper {

	@Override
	public Vector map(Vector inColor) {
		return new Vector(inColor.x / (inColor.x + 1), inColor.y / (inColor.y + 1), inColor.z / (inColor.z + 1));
	}

}
