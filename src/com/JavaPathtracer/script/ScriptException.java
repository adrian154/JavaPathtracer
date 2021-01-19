package com.JavaPathtracer.script;

public class ScriptException extends Exception {

	private static final long serialVersionUID = 1L;
	protected ASTNode sourceNode;
	
	public ScriptException(ASTNode node, String reason) {
		super(reason);
		this.sourceNode = node;
	}
	
}
