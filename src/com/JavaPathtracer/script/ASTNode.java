package com.JavaPathtracer.script;

public abstract class ASTNode {

	public abstract Object execute(Block block) throws ScriptException;
	
	protected void assertCond(boolean condition, String failureMessage) throws ScriptException {
		if(!condition)
			throw new ScriptException(this, failureMessage);
	}
	
	protected void assertNonNull(Object object, String message) throws ScriptException {
		assertCond(object != null, message);
	}
	
	protected Object checkType(Object object, Class<?> clazz) throws ScriptException {
		assertCond(object.getClass().isInstance(clazz), "Incompatible types: expected " + clazz.getName() + " but got " + object.getClass().getName());
		return object;
	}
	
}
