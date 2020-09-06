package com.JavaPathtracer.material;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JavaPathtracer.geometry.Vector;

public class Texture implements Sampleable {

	private BufferedImage texture;
	
	public Texture(int width, int height) {
		texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}
	
	public Texture(File imageFile) {
		
		try {
			texture = ImageIO.read(imageFile);
		} catch(IOException exception) {
			System.out.println("Failed to load texture from file \"" + imageFile.getName() + "\": " + exception.getMessage());
			exception.printStackTrace();
		}
		
	}
	
	public Texture(BufferedImage image) {
		this.texture = image;
	}
	
	public BufferedImage asImage() {
		return texture;
	}
	
	public Vector sample(double u, double v) {

		// TODO: Configurable interpolation
		int x = (int)Math.round(u * texture.getWidth());
		int y = (int)Math.round(v * texture.getHeight());
		
		// Clamp
		x = Math.max(Math.min(x, texture.getWidth() - 1), 0);
		y = Math.max(Math.min(y, texture.getHeight() - 1), 0);
		
		int rgb = texture.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;
		
		return new Vector(r / 255.0, g / 255.0, b / 255.0);
		
	}
	
	public void set(int x, int y, Vector color) {
		
		if(color.x < 0 || color.y < 0 || color.z < 0) {
			System.out.println("Warning: One or more color components were negative! " + color.toString());
		}
		
		int r = (int)Math.max(0, Math.min(color.x * 255, 255));
		int g = (int)Math.max(0, Math.min(color.y * 255, 255));
		int b = (int)Math.max(0, Math.min(color.z * 255, 255));
		int rgb = (r << 16) | (g << 8) | b;
		
		texture.setRGB(x, y, rgb);
		
	}
	
	public int getWidth() {
		return texture.getWidth();
	}
	
	public int getHeight() {
		return texture.getHeight();
	}
	
	public void saveToFile(File file) {
		
		try {
			ImageIO.write(texture, "png", file);
		} catch(IOException exception) {
			System.out.println("Failed to save texture to file \"" + file.getName() + "\": " + exception.getMessage());
			exception.printStackTrace();
		}
		
	}
	
}
