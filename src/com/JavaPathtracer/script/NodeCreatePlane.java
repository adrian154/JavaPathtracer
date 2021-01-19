package com.JavaPathtracer.script;

import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Vector;

public class NodeCreatePlane extends ASTNode {

	protected ASTNode pointNode, normalNode;
	
	public NodeCreatePlane(ASTNode pointNode, ASTNode normalNode) {
		this.pointNode = pointNode;
		this.normalNode = normalNode;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new Plane(
			(Vector)checkType(normalNode.execute(block), Vector.class),
			(Vector)checkType(pointNode.execute(block), Vector.class)			
		);
	}
	
}
