package com.JavaPathtracer.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import com.JavaPathtracer.renderer.Renderer.RenderJob;

public class PreviewPanel extends JPanel {

	// Appease the serialization gods
	public static final long serialVersionUID = 1;

	private RenderJob job;
	private int scale;

	public PreviewPanel(RenderJob job, int scale) {
		this.scale = scale;
		this.setPreferredSize(new Dimension(job.output.getWidth() * scale, job.output.getHeight() * scale));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		g.setFont(new Font("Consolas", Font.PLAIN, 16));
		g.setColor(new Color(0xFF00FF));
		
		// draw preview image
		g.drawImage(job.output.getScaledInstance(job.output.getWidth() * scale, job.output.getHeight() * scale, Image.SCALE_FAST), 0, 0, this);
		
		// draw debug text
		int y = 0;
		g.drawString("Raytracer: " + job.renderer.raytracer.toString(), 2, y += 16);
		g.drawString("Tonemapper: " + job.renderer.toneMapper.toString(), 2, y += 16);
		g.drawString(job.renderer.samples + " sample(s)", 2, y += 16);
		g.drawString("Position: " + job.renderer.scene.getCamera().getPos(), 2, y += 16);
		g.drawString("Looking direction: " + job.renderer.scene.getCamera().getLook(), 2, y += 16);
		
		/*
		if(!reducedDebug) {
			g.drawString(renderer.getRaytracer().getRays() + " rays traced", 2, y += 16);
			g.drawString(String.format("%.2f Mrays/second", (float)renderer.getRaytracer().getRays() / 1000 / (System.currentTimeMillis() - startTime)), 2, y += 16);
			g.drawString(String.format("%d/%d tiles (%d threads)", job.getCompletedTiles(), job.getInitTiles(), renderer.getThreads()), 2, y += 16);
		}
		*/
		
	}

}