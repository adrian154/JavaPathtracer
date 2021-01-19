package com.JavaPathtracer.script;

import com.JavaPathtracer.WorldObject;
import com.JavaPathtracer.geometry.Shape;
import com.JavaPathtracer.material.Material;

public class NodeAddObject extends ASTNode {

	protected ASTNode shapeNode, materialNode;
	
	public NodeAddObject(ASTNode shapeNode, ASTNode materialNode) {
		this.shapeNode = shapeNode;
		this.materialNode = materialNode;
	}
	
	public Object execute(Block block) throws ScriptException {
		
		block.getScene().add(new WorldObject(
			(Shape)checkType(this.shapeNode.execute(block), Shape.class),
			(Material)checkType(this.materialNode.execute(block), Material.class))
		);
		
		return null;
		
	}
	
}
