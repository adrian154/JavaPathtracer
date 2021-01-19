package com.JavaPathtracer.script;

import com.JavaPathtracer.material.DiffuseMaterial;
import com.JavaPathtracer.material.Sampleable;

public class NodeCreateDiffuseMat extends ASTNode {

	protected ASTNode colorNode;
	
	public NodeCreateDiffuseMat(ASTNode color) {
		this.colorNode = color;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new DiffuseMaterial((Sampleable)checkType(colorNode.execute(block), Sampleable.class));
	}
	
}
