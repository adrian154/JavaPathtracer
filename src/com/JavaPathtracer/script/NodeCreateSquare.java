package com.JavaPathtracer.script;

import com.JavaPathtracer.geometry.Circle;
import com.JavaPathtracer.geometry.Vector;

public class NodeCreateSquare extends ASTNode {

	protected ASTNode pointNode, normalNode, radiusNode;
	
	public NodeCreateSquare(ASTNode pointNode, ASTNode normalNode, ASTNode radiusNode) {
		this.pointNode = pointNode;
		this.normalNode = normalNode;
		this.radiusNode = radiusNode;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new Circle(
			(Vector)checkType(normalNode.execute(block), Vector.class),	
			(Vector)checkType(pointNode.execute(block), Vector.class),
			(Double)checkType(radiusNode.execute(block), Double.class)
		);
	}
	
}
