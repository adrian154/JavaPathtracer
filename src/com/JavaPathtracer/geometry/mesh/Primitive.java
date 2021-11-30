package com.JavaPathtracer.geometry.mesh;

import com.JavaPathtracer.geometry.BoundingBox;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.scene.WorldObject;

// cache bounding boxes to avoid gc thrashing
public class Primitive extends BoundingBox {

	public final WorldObject object;

	public Primitive(WorldObject object) {
		super(object.getBoundingBox());
		this.object = object;
	}

}
