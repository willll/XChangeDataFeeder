package tasks;

import java.util.ArrayList;

public class TasksAggregator implements Runnable{
	protected ArrayList<Runnable> tasks = new ArrayList<Runnable>();

	public TasksAggregator() {
		// nothing to do here
	}
	
	public TasksAggregator(Runnable task) {
		tasks.add(task);
	}
	
	public TasksAggregator(ArrayList<Runnable> tasks) {
		tasks.addAll(tasks);
	}
	
	@Override
	public void run() {
		tasks.forEach(t -> t.run());
	}
}
