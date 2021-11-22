package com.JavaPathtracer.pattern;

import java.io.File;
import java.io.IOException;

import com.JavaHDR.HDREncoder;
import com.JavaHDR.HDRImageRGB;
import com.JavaPathtracer.geometry.Vector;

public class HDRMap implements Sampleable {

	private HDRImageRGB image;
	private String path;

	public HDRMap(String path) throws IOException {
		this(new File(path));
	}
	
	public HDRMap(File file) throws IOException {
		this.path = file.getPath();
		image = (HDRImageRGB) HDREncoder.readHDR(file, true);
	}

	@Override
	public Vector sample(Vector textureCoord) {

		// TODO: Configurable interpolation
		int x = (int) Math.round(textureCoord.x * image.getWidth());
		int y = (int) Math.round(textureCoord.y * image.getHeight());

		// Clamp
		x = Math.max(Math.min(x, image.getWidth() - 1), 0);
		y = Math.max(Math.min(image.getHeight() - y - 1, image.getHeight() - 1), 0);

		float r = image.getPixelValue(x, y, 0);
		float g = image.getPixelValue(x, y, 1);
		float b = image.getPixelValue(x, y, 2);

		return new Vector(r*1.5, g*1.5, b*1.5);

	}
	
	@Override
	public String toString() {
		return String.format("HDRMap (%s)", path);
	}

}
