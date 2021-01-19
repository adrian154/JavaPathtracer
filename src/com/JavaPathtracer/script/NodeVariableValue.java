package com.JavaPathtracer.script;

public class NodeVariableValue extends ASTNode {

	protected final String identifier;
	
	public NodeVariableValue(String identifier) {
		this.identifier = identifier;
	}
	
	@Override
	public Object execute(Block block) throws ScriptException {
		Object value = block.get(identifier);
		assertNonNull(value, "Unknown variable \"" + identifier + "\"");
		return value;
	}
	
}
