package com.JavaPathtracer.renderers;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.JavaPathtracer.material.Texture;

public class LivePreviewPanel extends JPanel {

	// Appease the serialization gods
	public static final long serialVersionUID = 1;

	private Texture output;
	private int scale;

	public LivePreviewPanel(LivePreview preview, int scale) {
		this.output = preview.getImage();
		this.scale = scale;
		this.setPreferredSize(new Dimension(output.getWidth() * scale, output.getHeight() * scale));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(output.asImage().getScaledInstance(output.getWidth() * scale, output.getHeight() * scale,
				Image.SCALE_FAST), 0, 0, this);
	}

}