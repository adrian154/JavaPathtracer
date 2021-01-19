package com.JavaPathtracer.script;

import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Checkerboard;

public class NodeCreateCheckerboard extends ASTNode {

	protected ASTNode colorANode;
	protected ASTNode colorBNode;
	
	public NodeCreateCheckerboard(ASTNode A, ASTNode B) {
		this.colorANode = A;
		this.colorBNode = B;
	}
	
	@Override
	public Object execute(Block block) throws ScriptException {
		return new Checkerboard(
			(Vector)checkType(colorANode.execute(block), Vector.class),
			(Vector)checkType(colorBNode.execute(block), Vector.class)
		);
	}

}
