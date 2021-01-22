package com.JavaPathtracer;

public class Stopwatch {

	protected String taskName;
	protected long startTime;
	
	public Stopwatch(String taskName) {
		this.taskName = taskName;
		startTime = System.currentTimeMillis();
		System.out.println("Started \"" + taskName + "\"");
	}
	
	public void stop() {
		System.out.println("Finished \"" + taskName + "\" in " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
	public void lap(String lapName) {
		System.out.println("Reached \"" + taskName + "." + lapName + "\" in " + (System.currentTimeMillis() - startTime) + " milliseconds");
	}
	
}
