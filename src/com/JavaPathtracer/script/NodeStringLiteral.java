package com.JavaPathtracer.script;

public class NodeStringLiteral extends ASTNode {

	protected final String value;
	
	public NodeStringLiteral(String value) {
		this.value = value;
	}
	
	@Override
	public Object execute(Block block) {
		return value;
	}
	
}
