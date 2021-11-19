package com.JavaPathtracer.renderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.JavaPathtracer.renderer.Renderer.RenderJob;

public class LivePreview implements ActionListener {

	public final RenderJob renderJob;
	public final LivePreviewFrame frame;
	private Timer timer;

	public LivePreview(RenderJob job, int scale) {
		this.renderJob = job;
		this.frame = new LivePreviewFrame(this, scale);
	}

	public void start() {
		timer = new Timer(1 / 60, this);
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		frame.repaint();
	}
	
}