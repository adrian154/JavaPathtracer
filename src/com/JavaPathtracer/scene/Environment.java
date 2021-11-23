package com.JavaPathtracer.scene;

import com.JavaPathtracer.geometry.Vector;

// The sky is implemented using a separate interface because:
// ...performance is increased (expensive inverse trig functions don't need to be evaluated for static skies)
// ...unusual skies that cannot be described by simple Sampleables
public interface Environment {

	public Vector getEmission(Vector direction);
	
}
