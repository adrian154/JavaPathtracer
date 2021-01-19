package com.JavaPathtracer.script;

import java.awt.image.BufferedImage;

import com.JavaPathtracer.material.Texture;

public class NodeCreateEmptyTexture extends ASTNode {

	protected ASTNode xSizeNode;
	protected ASTNode ySizeNode;
	
	public NodeCreateEmptyTexture(ASTNode x, ASTNode y) {
		this.xSizeNode = x;
		this.ySizeNode = y;
	}
	
	public Object execute(Block block) throws ScriptException {
		return new Texture(new BufferedImage(
			(Integer)checkType(xSizeNode.execute(block), Integer.class),
			(Integer)checkType(ySizeNode.execute(block), Integer.class),
			BufferedImage.TYPE_INT_RGB
		));
	}
	
}
