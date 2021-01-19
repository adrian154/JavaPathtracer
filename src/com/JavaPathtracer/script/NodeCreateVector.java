package com.JavaPathtracer.script;

import com.JavaPathtracer.geometry.Vector;

public class NodeCreateVector extends ASTNode {
	
	protected ASTNode comp1, comp2, comp3;
	
	public NodeCreateVector(ASTNode comp1, ASTNode comp2, ASTNode comp3) {
		this.comp1 = comp1;
		this.comp2 = comp2;
		this.comp3 = comp3;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new Vector(
			(Double)checkType(comp1.execute(block), Double.class),
			(Double)checkType(comp2.execute(block), Double.class),
			(Double)checkType(comp3.execute(block), Double.class)
		);
	}
	
}
