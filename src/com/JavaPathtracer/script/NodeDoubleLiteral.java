package com.JavaPathtracer.script;

public class NodeDoubleLiteral extends ASTNode {

	protected final double value;
	
	public NodeDoubleLiteral(double value) {
		this.value = value;
	}
	
	@Override
	public Object execute(Block block) {
		return value;
	}
	
}
