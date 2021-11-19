package com.JavaPathtracer.renderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

import com.JavaPathtracer.renderer.Renderer.RenderJob;

public class LivePreview implements ActionListener {

	public final LivePreviewFrame frame;
	private Timer timer;

	public LivePreview(RenderJob job, int scale) {
		this.frame = new LivePreviewFrame(job, scale);
	}

	public void start() {
		timer = new Timer(1 / 60, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		frame.repaint();
	}
	
	private static class LivePreviewFrame extends JFrame {

		// (Ditto... see LivePreviewPanel)
		public static final long serialVersionUID = 1;
		
		public LivePreviewFrame(RenderJob job, int scale) {
		
			super();
			this.add(new PreviewPanel(job, scale));
			this.pack();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("Java Raytracer");
			this.setVisible(true);
			this.setResizable(false);
		
		}
		
	}

	
}