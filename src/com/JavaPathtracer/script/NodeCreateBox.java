package com.JavaPathtracer.script;

import com.JavaPathtracer.geometry.Plane;
import com.JavaPathtracer.geometry.Vector;

public class NodeCreateBox extends ASTNode {

	protected ASTNode minNode, maxNode;
	
	public NodeCreateBox(ASTNode minNode, ASTNode maxNode) {
		this.minNode = minNode;
		this.maxNode = maxNode;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new Plane(
			(Vector)checkType(minNode.execute(block), Vector.class),
			(Vector)checkType(maxNode.execute(block), Vector.class)			
		);
	}
	
}
