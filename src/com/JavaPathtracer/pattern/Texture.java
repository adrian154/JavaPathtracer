package com.JavaPathtracer.pattern;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JavaPathtracer.geometry.Vector;

public class Texture implements Sampleable {

	private String path;
	private BufferedImage texture;

	public Texture(int width, int height) {
		texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

	public Texture(File imageFile) throws IOException {
		texture = ImageIO.read(imageFile);
		this.path = imageFile.getPath();
	}

	public Texture(String path) throws IOException {
		this(new File(path)); 
	}
	
	public Texture(BufferedImage image) {
		this.texture = image;
	}

	public Channel getChannel(int channel) {
		return new Channel(this, channel);
	}
	
	@Override
	public Vector sample(Vector textureCoords) {

		// TODO: Configurable interpolation
		// TODO: texture wrap modes
		int x = (int) Math.floor((textureCoords.x + 1) % 1 * texture.getWidth());
		int y = (int) Math.floor((1 - (textureCoords.y + 1) % 1) * texture.getHeight());

		int rgb = texture.getRGB(x, y);
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;

		return new Vector(Math.pow(2.1, r / 255.0) - 1, Math.pow(2.1, g / 255.0) - 1, Math.pow(2.1, b / 255.0) - 1);

	}

	public int getWidth() {
		return texture.getWidth();
	}

	public int getHeight() {
		return texture.getHeight();
	}
	
	@Override
	public String toString() {
		return "Texture " + path;
	}

}
