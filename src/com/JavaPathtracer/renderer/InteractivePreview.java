package com.JavaPathtracer.renderer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import com.JavaPathtracer.cameras.Camera;
import com.JavaPathtracer.cameras.PerspectiveCamera;
import com.JavaPathtracer.geometry.Vector;
import com.JavaPathtracer.material.Texture;

public class InteractivePreview {

	protected static final double MOVEMENT_SCALE = 0.1;
	
	private Renderer renderer;
	private Texture output;
	private PreviewFrame frame;
	private Camera camera;
	private int scale;
	
	// controls
	private int prevX, prevY;
	private boolean forward, backward, left, right, up, down;
	private double azimuth, inclination, fov;
	
	public InteractivePreview(Renderer renderer, Camera camera, Texture output, int scale) {
		this.renderer = renderer;
		this.output = output;
		this.camera = camera;
		this.azimuth = Math.PI / 2;
		this.inclination = Math.PI / 2;
		fov = 30;
		this.scale = scale;
	}
	
	public void run() throws InterruptedException {
		this.frame = new PreviewFrame();
		while(true) {
			updateCamera();
			RenderJob job = renderer.render(output);
			job.await();
			frame.repaint();
			Thread.sleep(30 - System.currentTimeMillis() % 30);
		}
	}
	
	private void updateCamera() {
		
		//camera.setAngles(azimuth, inclination);
		camera.setAngles(azimuth, inclination);
		Vector fb = new Vector(Math.cos(azimuth), 0, Math.sin(azimuth));
		Vector lr = new Vector(Math.sin(azimuth), 0, -Math.cos(azimuth));
		
		int leftright = (left ? -1 : 0) + (right ? 1 : 0);
		int forwardback = (forward ? 1 : 0) + (backward ? -1 : 0);
		int updown = (up ? 1 : 0) + (down ? -1 : 0);
		camera.move(fb.times(forwardback * MOVEMENT_SCALE));
		camera.move(lr.times(leftright * MOVEMENT_SCALE));
		camera.move(new Vector(0, updown * MOVEMENT_SCALE, 0));
		if(camera instanceof PerspectiveCamera) {
			((PerspectiveCamera)camera).setFOV(fov);
		} 
		
	}

	private class PreviewFrame extends JFrame {
		
		private static final long serialVersionUID = 1L;

		public PreviewFrame() {
			
			super();
			this.add(new PreviewPanel());
			this.pack();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("Java Raytracer (Interactive)");
			this.setVisible(true);
			this.setResizable(false);
			this.createBufferStrategy(2);
			
			// add listeners
			MouseInput handler = new MouseInput();
			this.addMouseMotionListener(handler);
			this.addMouseListener(handler);
			this.addMouseWheelListener(handler);
			this.addKeyListener(new KeyInput());
			
		}
		
	}
	
	private class PreviewPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public PreviewPanel() {
			this.setPreferredSize(new Dimension(output.getWidth() * scale, output.getHeight() * scale));
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(output.asImage().getScaledInstance(output.getWidth() * scale, output.getHeight() * scale, Image.SCALE_FAST), 0, 0, this);
		}
		
	}
	
	private class MouseInput extends MouseInputAdapter {
		
		@Override
		public void mousePressed(MouseEvent event) {
			prevX = event.getX();
			prevY = event.getY();
		}
		
		@Override
		public void mouseDragged(MouseEvent event) {
			if(prevX != 0 && prevY != 0) {
				azimuth -= (event.getX() - prevX) * 0.01;
				inclination += (event.getY() - prevY) * 0.01;
			}
		
			azimuth = azimuth % (Math.PI * 2);
			inclination = Math.max(0, Math.min(inclination, Math.PI));
			prevX = event.getX();
			prevY = event.getY();
			
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent event) {
			fov += event.getWheelRotation();
		}
		
	}
	
	private class KeyInput extends KeyAdapter {
		
		private void onKey(int keycode, boolean state) {
			
			// handle control states
			switch(keycode) {
				case KeyEvent.VK_W: forward = state; break;
				case KeyEvent.VK_A: left = state; break;
				case KeyEvent.VK_S: backward = state; break;
				case KeyEvent.VK_D: right = state; break;
				case KeyEvent.VK_SPACE: up = state; break;
				case KeyEvent.VK_SHIFT: down = state; break;
			}
			
			// oneshot triggers
			if(state) {
				if(keycode == KeyEvent.VK_C) {
					System.out.printf("camera.moveTo(new Vector%s); camera.setAngles(%f, %f); camera.setFOV(%f);\n", camera.getPos().toString(), azimuth, inclination, fov);
				}
			}
			
		}
		
		@Override
		public void keyPressed(KeyEvent event) {
			onKey(event.getKeyCode(), true);
		}
		
		@Override
		public void keyReleased(KeyEvent event) {
			onKey(event.getKeyCode(), false);
		}
		
	}
	
}
