package com.JavaPathtracer.script;

import com.JavaPathtracer.material.EmissiveMaterial;
import com.JavaPathtracer.material.Sampleable;

public class NodeCreateEmissiveMat extends ASTNode {

	protected ASTNode colorNode;
	
	public NodeCreateEmissiveMat(ASTNode color) {
		this.colorNode = color;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new EmissiveMaterial((Sampleable)checkType(colorNode.execute(block), Sampleable.class));
	}
	
}
