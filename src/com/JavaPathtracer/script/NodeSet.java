package com.JavaPathtracer.script;

public class NodeSet extends ASTNode {

	protected ASTNode identifierNode, valueNode;
	
	public NodeSet(ASTNode identifier, ASTNode value) {
		this.identifierNode = identifier;
		this.valueNode = value;
	}
	
	public Object execute(Block block) throws ScriptException {
		
		String identifier = (String)checkType(identifierNode.execute(block), String.class);
		Object value = valueNode.execute(block);
		
		block.set(identifier, value);
		return value;
		
	}
	
}
