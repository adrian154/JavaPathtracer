package com.JavaPathtracer.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.JavaPathtracer.geometry.Hit;
import com.JavaPathtracer.geometry.Ray;
import com.JavaPathtracer.material.Texture;

public class LivePreviewPanel extends JPanel {

	// Appease the serialization gods
	public static final long serialVersionUID = 1;

	private RenderJob job;
	private Texture output;
	private long startTime;
	private int scale;

	public LivePreviewPanel(LivePreview preview, int scale) {
		this.output = preview.getImage();
		this.job = preview.getRenderJob();
		this.scale = scale;
		this.startTime = System.currentTimeMillis();
		this.setPreferredSize(new Dimension(output.getWidth() * scale, output.getHeight() * scale));
		this.addMouseListener(new PreviewMouseListener());
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font("Consolas", Font.PLAIN, 16));
		g.setColor(new Color(0xFF00FF));
		g.drawImage(output.asImage().getScaledInstance(output.getWidth() * scale, output.getHeight() * scale, Image.SCALE_FAST), 0, 0, this);
		g.drawString("Raytracer: " + job.getRaytracer().toString(), 2, 16);
		g.drawString("Tonemapper: " + job.getRenderer().getTonemapper().toString(), 2, 32);
		g.drawString(job.getSamples() + " samples", 2, 48);
		g.drawString(job.getRays() + " rays traced", 2, 64);
		g.drawString(String.format("%.2f Mrays/second", (float)job.getRays() / 1000 / (System.currentTimeMillis() - startTime)), 2, 80);
		g.drawString(String.format("%d/%d tiles (%d threads)", job.getCompletedTiles(), job.getInitTiles(), job.getThreads()), 2, 96);
	}
	
	private class PreviewMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent event) {
			
			int maxdim = Math.min(output.getWidth(), output.getHeight());
			Ray ray = job.getRenderer().getCamera().getCameraRay(event.getX() / scale, output.getHeight() - event.getY() / scale - 1, maxdim, 0, 0);
			Hit hit = job.getRenderer().getScene().traceRay(ray);

			//System.out.println(ray.origin + ", " + ray.direction);
			System.out.println(hit.hit + ", " + hit.point + ", " + hit.normal);
			
		}
		
	}

}