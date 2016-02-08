package jobshed;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Task {

	private final Task parent;	
	private final Set<Task> children = new HashSet<>();
	private final float cost;
		
	public Task(float cost) {
		parent = null;
		this.cost = cost;
	}
	
	public Task(float cost, Task parent) {
		this.parent = parent;
		this.cost = cost;
	}
	
	public boolean hasParent() {
		if (parent == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public float getThisCost() {
		return cost;
	}
	
	public float getTotalCost() {
		float total = cost;
		for (Task t : children) {
			total += t.getTotalCost();
		}
		return total;
	}
	
	@Override 
	public boolean equals(Object other) { 
		if (other == this) { 
			return true; 
		} 
		if (other == null || !(other instanceof Task)) { 
			return false; 
		}
		Task otherTask = (Task) other;
		if (Objects.equals(this.parent, otherTask.parent)
				&& Objects.equals(this.children, otherTask.children)
				&& Objects.equals(this.cost, otherTask.cost)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public int hashCode() {
		return Objects.hash(parent, children, cost);
	}
}
