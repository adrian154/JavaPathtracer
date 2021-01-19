package com.JavaPathtracer.script;

import com.JavaPathtracer.material.SampleableScalar;

public class NodeCreateRatio extends ASTNode {

	protected final ASTNode ratioNode;
	
	public NodeCreateRatio(ASTNode ratioNode) {
		this.ratioNode = ratioNode;
	}
	
	@Override
	public Object execute(Block block) throws ScriptException {
		return (SampleableScalar)checkType(ratioNode.execute(block), SampleableScalar.class);
	}
	
}
