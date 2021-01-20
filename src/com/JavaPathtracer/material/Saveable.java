package com.JavaPathtracer.material;

import java.io.File;
import java.io.IOException;

public interface Saveable {

	public void saveToFile(File file) throws IOException;
	
}
