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
	private Texture output;
	private long startTime;
	private int scale;
	private boolean reducedDebug;

	public LivePreviewPanel(LivePreview preview, int scale, boolean reducedDebug) {
		this.output = preview.getImage();
		this.reducedDebug = reducedDebug;
		this.job = preview.getRenderJob();
		this.scale = scale;
		this.startTime = System.currentTimeMillis();
		this.setPreferredSize(new Dimension(output.getWidth() * scale, output.getHeight() * scale));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.setFont(new Font("Consolas", Font.PLAIN, 16));
		g.setColor(new Color(0xFF00FF));
		
		// draw preview image
		g.drawImage(output.asImage().getScaledInstance(output.getWidth() * scale, output.getHeight() * scale, Image.SCALE_FAST), 0, 0, this);
		
		// draw debug text
		int y = 0;
		Renderer renderer = job.getRenderer();
		g.drawString(renderer.getSamples() + " sample(s)", 2, y += 16);
		if(!reducedDebug) {
			g.drawString(renderer.getRaytracer().raysTraced + " rays traced", 2, y += 16);
			g.drawString(String.format("%.2f Mrays/second", (float)renderer.getRaytracer().raysTraced / 1000 / (System.currentTimeMillis() - startTime)), 2, y += 16);
			g.drawString(String.format("%d/%d tiles (%d threads)", job.getCompletedTiles(), job.getInitTiles(), renderer.getThreads()), 2, y += 16);
		}
		
	}

}