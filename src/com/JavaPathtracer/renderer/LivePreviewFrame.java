package com.JavaPathtracer.renderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class LivePreviewFrame extends JFrame implements ActionListener {

	// (Ditto... see LivePreviewPanel)
	public static final long serialVersionUID = 1;
	private Timer timer;
	
	public LivePreviewFrame(LivePreview preview, int scale) {
	
		super();
		this.add(new LivePreviewPanel(preview, scale));
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Java Raytracer");
		this.setVisible(true);
		this.setResizable(false);
		
		timer = new Timer(1 / 60, this);
		timer.start();
	
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.repaint();
	}
	
}
