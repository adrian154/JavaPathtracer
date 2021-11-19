package com.JavaPathtracer.pattern;

import java.io.File;
import java.io.IOException;

import com.JavaHDR.HDREncoder;
import com.JavaHDR.HDRImageRGB;
import com.JavaPathtracer.geometry.Vector;

public class HDRMap implements Sampleable {

	private HDRImageRGB image;
	private String path;

	public HDRMap(File file) {

		this.path = file.getPath();
		
		try {
			image = (HDRImageRGB) HDREncoder.readHDR(file, true);
		} catch (IOException exception) {
			System.out.println("Failed to load HDR map from \"" + file.getName() + "\": " + exception.getMessage());
			exception.printStackTrace();
		}

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
