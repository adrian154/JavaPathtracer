package com.JavaPathtracer.renderers;

import javax.swing.JFrame;

import com.JavaPathtracer.material.Texture;

public class LivePreviewFrame extends JFrame {
	
	// (Ditto... see LivePreviewPanel)
	public static final long serialVersionUID = 1;
	
	public LivePreviewFrame(Texture output, int scale) {
		super();
		this.add(new LivePreviewPanel(output, scale));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Java Raytracer");
		this.setVisible(true);
	}
	
}
