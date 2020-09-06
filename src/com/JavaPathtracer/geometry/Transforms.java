package com.JavaPathtracer.geometry;

public class Transforms {

	public static Vector localToWorldCoords(Vector vector, Vector bvx, Vector bvy, Vector bvz) {
		return new Vector(
			vector.x * bvx.x + vector.y * bvy.x + vector.z * bvz.x,
			vector.x * bvx.y + vector.y * bvy.y + vector.z * bvz.y,
			vector.x * bvx.z + vector.y * bvy.z + vector.z * bvz.z
		);
	}
	
}
