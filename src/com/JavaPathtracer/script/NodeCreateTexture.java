package com.JavaPathtracer.script;

import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.material.Texture;

public class NodeCreateTexture extends ASTNode {

	protected ASTNode textureNameNode;
	
	public NodeCreateTexture(ASTNode textureName) {
		this.textureNameNode = textureName;
	}
	
	public Object execute(Block block) throws ScriptException {
		try {
			return new Texture(new File((String)checkType(textureNameNode.execute(block), String.class)));
		} catch(IOException exception) {
			throw new ScriptException(this, "Failed to load texture from file: " + exception.getMessage());
		}
	}
	
}
