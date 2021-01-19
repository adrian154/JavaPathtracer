package com.JavaPathtracer.script;

import com.JavaPathtracer.material.MirrorMaterial;
import com.JavaPathtracer.material.Sampleable;

public class NodeCreateMirrorMat extends ASTNode {

	protected ASTNode colorNode;
	
	public NodeCreateMirrorMat(ASTNode color) {
		this.colorNode = color;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new MirrorMaterial((Sampleable)checkType(colorNode.execute(block), Sampleable.class));
	}
	
}
