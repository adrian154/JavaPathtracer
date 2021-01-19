package com.JavaPathtracer.script;

import com.JavaPathtracer.material.RoughMaterial;
import com.JavaPathtracer.material.Sampleable;
import com.JavaPathtracer.material.SampleableDouble;

public class NodeCreateRoughMat extends ASTNode {

	protected ASTNode colorNode;
	protected ASTNode roughnessNode;
	
	public NodeCreateRoughMat(ASTNode color, ASTNode roughness) {
		this.colorNode = color;
		this.roughnessNode = roughness;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new RoughMaterial(
			(Sampleable)checkType(colorNode.execute(block), Sampleable.class),
			(SampleableDouble)checkType(roughnessNode.execute(block), SampleableDouble.class)
		);
	}
	
}
