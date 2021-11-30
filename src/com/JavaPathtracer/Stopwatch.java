package com.JavaPathtracer;
import java.util.ArrayDeque;
import java.util.Deque;

// *absolutely* not thread safe, should only be used for gross timing 
public class Stopwatch {

	private static final Deque<Task> tasks = new ArrayDeque<>();
	
	// we don't need nanosecond accuracy, but System#nanoTime is guaranteed to be monotonic
	public static void start(String taskName) {
		tasks.push(new Task(taskName, System.nanoTime()));
		System.out.printf("Started %s\n", taskName);
	}
	
	public static void finish() {
		Task task = tasks.pop();
		System.out.printf("Finished %s in %d ms\n", task.name, (System.nanoTime() - task.startTime) / 1000000);
	}
	
	// mostly for housekeeping purposes
	public static void cleanup() {
		if(!tasks.isEmpty()) {
			System.out.println("WARNING: cleanup() called, but some tasks were not marked as finished:");
			for(Task task: tasks) {
				System.out.println(task.name);
			}
		}
	}
	
	private static class Task {
		
		public final String name;
		public final long startTime;
		
		public Task(String name, long startTime) {
			this.name = name;
			this.startTime = startTime;
		}
		
	}
	
}
