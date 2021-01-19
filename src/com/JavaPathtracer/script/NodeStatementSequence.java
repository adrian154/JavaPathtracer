package com.JavaPathtracer.script;

import java.util.List;

public class NodeStatementSequence extends ASTNode {

	protected List<ASTNode> children;
	
	public NodeStatementSequence(List<ASTNode> children) {
		this.children = children;
	}
	
	public Object execute(Block block) throws ScriptException {
		
		for(ASTNode node: children) {
			node.execute(block);
		}
		
		return null;
		
	}
	
}
