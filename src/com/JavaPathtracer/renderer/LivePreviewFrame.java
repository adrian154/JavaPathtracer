package com.JavaPathtracer.renderer;

import javax.swing.JFrame;

public class LivePreviewFrame extends JFrame {

	// (Ditto... see LivePreviewPanel)
	public static final long serialVersionUID = 1;
	
	public LivePreviewFrame(LivePreview preview, int scale) {
	
		super();
		this.add(new LivePreviewPanel(preview, scale, false));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Java Raytracer");
		this.setVisible(true);
		this.setResizable(false);
	
	}
	
}
