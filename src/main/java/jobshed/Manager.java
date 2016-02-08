package jobshed;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Manager {

	
	
	private final Comparator<Task> costComparator = new Comparator<Task>() {
		@Override
		public int compare(Task t0, Task t1) {
			return Float.compare(t0.getTotalCost(), t1.getTotalCost());
		}
	};
	private final Queue<Task> tasks = new PriorityQueue<>(11, costComparator);
	
	public Manager() {
		// TODO Auto-generated constructor stub
	}
	
	public void add(Task t) {
		if (t.hasParent()) throw new IllegalArgumentException("Can only add top tier tasks to manager");
		tasks.add(t);
	}
	
	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
	 * 
	 * @return 
	 */
	public Task peek() {
		return tasks.peek();
	}
	
	/**
	 * Retrieves and removes the head of this queue, or returns null if this queue is empty.
	 * 
	 * @return 
	 */
	public Task poll() {
		return tasks.poll();
	}

}
