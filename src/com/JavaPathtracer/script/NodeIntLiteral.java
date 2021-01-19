package com.JavaPathtracer.script;

public class NodeIntLiteral extends ASTNode {

	protected final int value;
	
	public NodeIntLiteral(int value) {
		this.value = value;
	}
	
	@Override
	public Object execute(Block block) {
		return value;
	}
	
}
