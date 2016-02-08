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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Manager<T> {	
	
	protected final Comparator<Task<T>> costComparator = new Comparator<Task<T>>() {
		@Override
		public int compare(Task<T> t0, Task<T> t1) {
			return Float.compare(t0.getTotalCost(), t1.getTotalCost());
		}
	};
	protected final Queue<Task<T>> tasks = new PriorityQueue<>(costComparator);
	
	protected final Set<Agent<T>> agents = new HashSet<>();

    private static Logger log = LoggerFactory.getLogger(Manager.class);
    
	public Manager() {
		// TODO Auto-generated constructor stub
	}
	
	public void add(Task<T> t) {
		if (t.hasParent()) throw new IllegalArgumentException("Can only add top tier tasks to manager");
		tasks.add(t);
	}
	
	/**
	 * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
	 * 
	 * @return 
	 */
	public Task<T> peek() {
		return tasks.peek();
	}
	
	/**
	 * Retrieves and removes the head of this queue, or returns null if this queue is empty.
	 * 
	 * @return 
	 */
	public Task<T> poll() {
		return tasks.poll();
	}
	
	public void remove(Task<T> task) {
		if (task.hasParent()) {
			task.parent.removeChild(task);
		} else {
			if (tasks.contains(task)) {
				tasks.remove(task);
			} else {
				throw new IllegalArgumentException("Attempting to complete task that is not present");
			}
		}
	}

	
	public void add(Agent<T> a) {
		agents.add(a);
	}
	
	public class AvaliableTaskIterator implements Iterator<Task<T>> {
		
		private List<Iterator<Task<T>>> listOfIterators = new ArrayList<>();
		
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
		public Task<T> next() {
			if (listOfIterators.size() == 0) {
				throw new NoSuchElementException();
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			
			Task<T> next =  listOfIterators.get(listOfIterators.size()-1).next();
			
			if (next.children.size() > 0) {
				listOfIterators.add(next.children.iterator());
				return next(); //recurse into newly added iterator
			} else {
				return next;
			}
		}
		
	}
	
	public Iterator<Task<T>> getAvaliableTasks() {
		return new AvaliableTaskIterator();
	}
	
	
	/**
	 * Handles assigning tasks to agents, starting with tasks that have the lowest cost and no children
	 */
	public void assign() {
		for (Agent<T> agent: agents) {
			Iterator<Task<T>> taskIterator = getAvaliableTasks();
			while (taskIterator.hasNext()) {
				Task<T> t = taskIterator.next();
				if (!t.isAssigned()) {
					t.setAssignedTo(agent);
					break; //end task loop, go to next agent
				}
			}
		}
	}
}
