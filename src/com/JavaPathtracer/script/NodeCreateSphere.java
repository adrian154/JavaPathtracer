package com.JavaPathtracer.script;

import com.JavaPathtracer.geometry.Sphere;
import com.JavaPathtracer.geometry.Vector;

public class NodeCreateSphere extends ASTNode {

	protected ASTNode centerNode, radiusNode;
	
	public NodeCreateSphere(ASTNode centerNode, ASTNode radiusNode) {
		this.centerNode = centerNode;
		this.radiusNode = radiusNode;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new Sphere(
			(Vector)checkType(centerNode.execute(block), Vector.class),
			(Double)checkType(radiusNode.execute(block), Double.class)
		);
	}
	
}
