package com.JavaPathtracer;

public class Stopwatch {

	protected String taskName;
	protected long startTime;
	
	public Stopwatch(String taskName) {
		this.taskName = taskName;
		startTime = System.currentTimeMillis();
		System.out.println("START: " + taskName + "");
	}
	
	public void stop() {
		System.out.println("FINISH: " + taskName + " (" + (System.currentTimeMillis() - startTime) + "ms)");
	}
	
	public void lap(String lapName) {
		System.out.println("REACHED: " + taskName + "." + lapName + " (" + (System.currentTimeMillis() - startTime) + "ms)");
	}
	
}
