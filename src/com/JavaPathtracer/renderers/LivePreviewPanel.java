package com.JavaPathtracer.renderers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.JavaPathtracer.material.Texture;

public class LivePreviewPanel extends JPanel implements ActionListener {

	// Appease the serialization gods
	public static final long serialVersionUID = 1;

	private Timer timer;
	private Texture output;
	private int scale;

	public LivePreviewPanel(Texture output, int scale) {
		this.output = output;
		this.timer = new Timer(1 / 60, this);
		this.scale = scale;
		this.timer.start();
		this.setPreferredSize(new Dimension(output.getWidth() * scale, output.getHeight() * scale));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(output.asImage().getScaledInstance(output.getWidth() * scale, output.getHeight() * scale,
				Image.SCALE_FAST), 0, 0, this);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		this.repaint();
	}

}
