package com.JavaPathtracer.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.JavaPathtracer.material.Texture;

public class LivePreviewPanel extends JPanel {

	// Appease the serialization gods
	public static final long serialVersionUID = 1;

	private RenderJob job;
	private String raytracerName;
	private Texture output;
	private long startTime;
	private int scale;

	public LivePreviewPanel(LivePreview preview, int scale) {
		this.output = preview.getImage();
		this.job = preview.getRenderJob();
		this.raytracerName = job.getRaytracer().getName();
		this.scale = scale;
		this.startTime = System.currentTimeMillis();
		this.setPreferredSize(new Dimension(output.getWidth() * scale, output.getHeight() * scale));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font("Consolas", Font.PLAIN, 16));
		g.setColor(new Color(0xFF0000));
		g.drawImage(output.asImage().getScaledInstance(output.getWidth() * scale, output.getHeight() * scale, Image.SCALE_FAST), 0, 0, this);
		g.drawString(raytracerName, 2, 16);
		g.drawString(job.getSamples() + " samples", 2, 32);
		g.drawString(job.getRays() + " rays traced", 2, 48);
		g.drawString(String.format("%.2f Mrays/second", (float)job.getRays() / 1000 / (System.currentTimeMillis() - startTime)), 2, 64);
		g.drawString(String.format("%d/%d tiles (%d threads)", job.getCompletedTiles(), job.getInitTiles(), job.getThreads()), 2, 80);
	}

}