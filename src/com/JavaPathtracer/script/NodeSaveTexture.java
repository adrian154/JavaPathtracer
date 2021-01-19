package com.JavaPathtracer.script;

import java.io.File;
import java.io.IOException;

import com.JavaPathtracer.material.Texture;

public class NodeSaveTexture extends ASTNode {

	protected ASTNode textureNode;
	protected ASTNode nameNode;
	
	public NodeSaveTexture(ASTNode texture, ASTNode name) {
		this.textureNode = texture;
		this.nameNode = name;
	}
	
	@Override
	public Object execute(Block block) throws ScriptException {
		
		Texture texture = (Texture)checkType(textureNode.execute(block), Texture.class);
		String name = (String)checkType(nameNode.execute(block), String.class);
		
		try {
			texture.saveToFile(new File("name"));
		} catch(IOException exception) {
			throw new ScriptException(this, "Failed to save texture to file: " + name);
		}
		
		return null;
		
	}
	
}
