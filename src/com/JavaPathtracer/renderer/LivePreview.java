package com.JavaPathtracer.renderer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.JavaPathtracer.pattern.Texture;

public class LivePreview implements ActionListener {

	private int scale;
	private Texture output;
	private RenderJob job;
	private LivePreviewFrame frame;
	private Timer timer;
	
	public LivePreview(RenderJob renderJob, int scale) {
		this.output = renderJob.getOutput();
		this.job = renderJob;
		this.scale = scale;
	}

	public void start() {
		this.frame = new LivePreviewFrame(this, scale);
		timer = new Timer(1 / 60, this);
		timer.start();
	}

	public LivePreviewFrame getPreviewFrame() {
		return this.frame;
	}
	
	public Texture getImage() {
		return this.output;
	}

	public RenderJob getRenderJob() {
		return this.job;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		frame.repaint();
	}
	
}