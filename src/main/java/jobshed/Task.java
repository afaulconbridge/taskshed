package jobshed;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Task {

	protected Task parent = null;
	protected final Set<Task> children = new HashSet<>();
	protected final float cost;
	protected Agent assignedTo;
		
	protected Task(float cost, Set<Task> children) {
		this.cost = cost;
		if (children != null && children.size() > 0) {
			this.children.addAll(children);
		}
	}

	public Agent getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(Agent agent) {
		if (agent.hasCurrentTask() && agent.getCurrentTask() != this) {
			throw new IllegalArgumentException("Cannot assign to working agent");
		}
		this.assignedTo = agent;
		if (agent.getCurrentTask() != this) {
			agent.setCurrentTask(this);
		}
	}
	
	public boolean isAssigned() {
		if (assignedTo == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean hasParent() {
		if (parent == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public Set<Task> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	public void setParent(Task t) {
		if (this.parent != null) {
			throw new IllegalArgumentException("Attempt to set already existing parent");
		}
		//TODO check for loops
		this.parent = t;
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
		if (Objects.equals(this.children, otherTask.children)
				&& Objects.equals(this.cost, otherTask.cost)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public int hashCode() {
		return Objects.hash(children, cost);
	}

	public void removeChild(Task t) {
		if (children.contains(t)) {
			children.remove(t);
		} else {
			throw new IllegalArgumentException("Attempting to remove non-child task");
		}
	}
	
	public static Task create(float cost, Task... children ) {
		Set<Task> childrenSet = new HashSet<>();
		for (Task child : children) {
			childrenSet.add(child);
		}
		
		Task t = new Task(cost, childrenSet);
		if (childrenSet.size() > 0) {
			for (Task child : childrenSet) {
				child.setParent(t);
			}
		}
		return t;
	}

}
