package com.JavaPathtracer.pattern;

import com.JavaPathtracer.geometry.Vector;

public class Channel implements SampleableScalar {

	private Sampleable source;
	private int channel;
	
	public Channel(Sampleable source, int channel) {
		this.source = source;
		this.channel = channel;
	}
	
	@Override
	public double sampleScalar(Vector textureCoords) {
		return source.sample(textureCoords).get(channel);
	}
	
}
