package com.JavaPathtracer.script;

import com.JavaPathtracer.material.Material;
import com.JavaPathtracer.material.MixMaterial;
import com.JavaPathtracer.material.SampleableScalar;

public class NodeCreateMixMat extends ASTNode {

	protected ASTNode matANode;
	protected ASTNode matBNode;
	protected ASTNode mixNode;
	
	public NodeCreateMixMat(ASTNode A, ASTNode B, ASTNode mix) {
		this.matANode = A;
		this.matBNode = B;
		this.mixNode = mix;
	}
	
	@Override
	public Object execute(Block block) throws ScriptException {
		return new MixMaterial(
			(Material)checkType(matANode.execute(block), Material.class),
			(Material)checkType(matBNode.execute(block), Material.class),
			(SampleableScalar)checkType(mixNode.execute(block), SampleableScalar.class)
		);
	}
	
}
