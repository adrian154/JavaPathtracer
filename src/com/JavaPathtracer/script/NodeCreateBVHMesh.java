package com.JavaPathtracer.script;

import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.geometry.BVHMesh;

public class NodeCreateBVHMesh extends ASTNode {

	protected ASTNode meshNameNode;
	
	public NodeCreateBVHMesh(ASTNode meshNameNode) {
		this.meshNameNode = meshNameNode;
	}
	
	@Override
	public Object execute(Block block) throws ScriptException {
		try {
			return new BVHMesh(new File((String)checkType(meshNameNode.execute(block), String.class)));
		} catch(IOException exception) {
			throw new ScriptException(this, "Failed to load mesh from file: " + exception.getMessage());
		}
	}
	
}
