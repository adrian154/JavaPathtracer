package com.JavaPathtracer.material;

import java.io.File;
import java.io.IOException;

import com.JavaHDR.HDREncoder;
import com.JavaHDR.HDRImageRGB;
import com.JavaPathtracer.geometry.Vector;

public class HDRMap implements Sampleable, Saveable {

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
	public Vector sample(double u, double v) {

		// TODO: Configurable interpolation
		int x = (int) Math.round(u * image.getWidth());
		int y = (int) Math.round(v * image.getHeight());

		// Clamp
		x = Math.max(Math.min(x, image.getWidth() - 1), 0);
		y = Math.max(Math.min(image.getHeight() - y - 1, image.getHeight() - 1), 0);

		float r = image.getPixelValue(x, y, 0);
		float g = image.getPixelValue(x, y, 1);
		float b = image.getPixelValue(x, y, 2);

		return new Vector(r, g, b);

	}
	
	@Override
	public void saveToFile(File file) throws IOException {
		HDREncoder.writeHDR(image, file);
	}
	
	@Override
	public String toString() {
		return String.format("HDRMap (%s)", path);
	}

}
