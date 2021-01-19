package com.JavaPathtracer.script;

import java.util.HashMap;
import java.util.Map;

import com.JavaPathtracer.Scene;

public class Block {

	protected Map<String, Object> objects;
	protected Block parent;
	protected Scene theScene;
	
	public Block() {
		this.objects = new HashMap<String, Object>();
	}
	
	public Block(Scene scene) {
		this();
		this.theScene = scene;
	}
	
	public Block(Block parent) {
		this(parent.getScene());
		this.parent = parent;
	}
	
	public Scene getScene() {
		return theScene;
	}
	
	public Object get(String identifier) {
		
		if(objects.containsKey(identifier)) {
			return objects.get(identifier);
		}
		
		if(parent != null) {
			return parent.get(identifier);
		}
		
		return null;
		
	}
	
	public void set(String identifier, Object object) {
		if(!parent.setIfHaskey(identifier, object)) {
			objects.put(identifier, object);
		}
	}
	
	public boolean setIfHaskey(String identifier, Object object) {
		
		if(objects.containsKey(identifier)) {
			objects.put(identifier, object);
			return true;
		}
		
		if(parent != null) {
			return parent.setIfHaskey(identifier, object);
		}
		
		return false;
		
	}
	
}
