package jobshed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Task<T> {

	protected Task<?> parent = null;
	protected final Set<Task<?>> children = new HashSet<>(); 
	protected final float cost;
	protected Agent assignedTo;
	private final T content;
		
	protected Task(T content, float cost, Set<Task<T>> children) {
		this.content = content;
		this.cost = cost;
		if (children != null && children.size() > 0) {
			this.children.addAll(children);
		}
	}

	public Agent getAssignedTo() {
		return assignedTo;
	}
	
	public T getContent() {
		return content;
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
	
	public Set<Task<?>> getChildren() {
		return Collections.unmodifiableSet(children);
	}

	public void setParent(Task<T> t) {
		if (this.parent != null) {
			throw new IllegalArgumentException("Attempt to set already existing parent ("+t+") in "+this);
		}
		//TODO check for loops
		this.parent = t;
	}
	
	public float getThisCost() {
		return cost;
	}
	
	public float getTotalCost() {
		float total = cost;
		for (Task<?> t : children) {
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
		Task<?> otherTask = (Task<?>) other;
		if (Objects.equals(this.content, otherTask.content)
				&& Objects.equals(this.children, otherTask.children)
				&& Objects.equals(this.cost, otherTask.cost)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
    public int hashCode() {
		//cannot hash on children because it may change
		return Objects.hash(content, cost);
	}
	
	public String toString() {
		String str = "Task("+content+", "+cost;
		for (Task<?> task : children) {	
			str = str+", "+task;
		}
		str = str+")";
		return str;
	}

	public void removeChild(Task<?> task) {
		if (children.contains(task)) {
			children.remove(task);
		} else {
			throw new IllegalArgumentException("Attempting to remove non-child task ("+task+") from "+this);
		}
	}
	
	@SafeVarargs //needed because vaargs in children is interpreted as an array without the typing information
	public static <T> Task<T> create(T content, float cost, Task<T>... children ) {
		Set<Task<T>> childrenSet = new HashSet<>(Arrays.asList(children));
		
		Task<T> t = new Task<T>(content, cost, childrenSet);
		if (childrenSet.size() > 0) {
			for (Task<T> child : childrenSet) {
				child.setParent(t);
			}
		}
		return t;
	}

}
