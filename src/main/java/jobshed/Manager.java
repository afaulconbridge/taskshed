package jobshed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Manager {	
	
	protected final Comparator<Task> costComparator = new Comparator<Task>() {
		@Override
		public int compare(Task t0, Task t1) {
			return Float.compare(t0.getTotalCost(), t1.getTotalCost());
		}
	};
	protected final Queue<Task> tasks = new PriorityQueue<>(costComparator);
	
	protected final Set<Agent> agents = new HashSet<>();
	
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
	
	public void remove(Task t) {
		if (t.hasParent()) {
			t.parent.removeChild(t);
		} else {
			if (tasks.contains(t)) {
				tasks.remove(t);
			} else {
				throw new IllegalArgumentException("Attempting to complete task that is not present");
			}
		}
	}

	
	public void add(Agent a) {
		agents.add(a);
	}
	
	private class AvaliableTaskIterator implements Iterator<Task> {
		
		private List<Iterator<Task>> listOfIterators = new ArrayList<>();
		
		public AvaliableTaskIterator() {
			listOfIterators.add(tasks.iterator());
		}

		@Override
		public boolean hasNext() {
			if (listOfIterators.size() == 0) {
				return false;
			}
			//remove empty iterators from the end of the list
			while (!listOfIterators.get(listOfIterators.size()-1).hasNext()) {
				listOfIterators.remove(listOfIterators.size()-1);
				if (listOfIterators.size() == 0) {
					return false;
				}
			}
			//either we are at a stack of iterators where the tail is non-empty
			//or we have already returned false because the stack is empty
			return true;
		}

		@Override
		public Task next() {
			if (listOfIterators.size() == 0) {
				throw new NoSuchElementException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			Iterator<Task> lastIterator = listOfIterators.get(listOfIterators.size()-1);
			
			Task next = lastIterator.next();
			
			if (next.children.size() > 0) {
				listOfIterators.add(next.children.iterator());
				return next(); //recurse into newly added iterator
			} else {
				return next;
			}
		}
		
	}
	
	public Iterator<Task> getAvaliableTasks() {
		return new AvaliableTaskIterator();
	}
	
	
	/**
	 * Handles assigning tasks to agents, starting with tasks that have the lowest cost and no children
	 */
	public void assign() {
		for (Agent agent: agents) {
			Iterator<Task> taskIterator = getAvaliableTasks();
			while (taskIterator.hasNext()) {
				Task t = taskIterator.next();
				if (!t.isAssigned()) {
					t.setAssignedTo(agent);
					break; //end task loop, go to next agent
				}
			}
		}
	}
}
