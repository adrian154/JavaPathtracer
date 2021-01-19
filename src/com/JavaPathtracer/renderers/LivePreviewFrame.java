package com.JavaPathtracer.renderers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class LivePreviewFrame extends JFrame implements ActionListener {

	// (Ditto... see LivePreviewPanel)
	public static final long serialVersionUID = 1;
	private Renderer renderer;
	private Timer timer;
	
	public LivePreviewFrame(LivePreview preview, int scale) {
		
		super();

		this.renderer = preview.getRenderer();
		
		this.add(new LivePreviewPanel(preview, scale));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Java Raytracer");
		this.setVisible(true);
		
		timer = new Timer(1 / 60, this);
		timer.start();
	
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.repaint();
		this.setTitle(String.format("JavaPathtracer - %d samples/%d threads - %d rays traced", renderer.getSamples(), renderer.getThreads(), renderer.getRaytracer().getRays()));
	}
	
}
