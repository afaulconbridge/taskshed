package jobshed;

import java.util.HashSet;
import java.util.Set;

public class TaskLabelled extends Task {

	private final String label;

	protected TaskLabelled(String label, float cost, Set<Task> children) {
		super(cost, children);
		this.label = label;
	}
	
	@Override
	public String toString() {
		String out = "TaskLabelled(\""+label+"\", "+cost;
		for (Task child : children) {
			out +=", "+child;
		}
		out = out +")";
		return out;
	}
	
	public static TaskLabelled create(String label, float cost, Task... children) {
		Set<Task> childrenSet = new HashSet<>();
		for (Task child : children) {
			childrenSet.add(child);
		}
		
		TaskLabelled t = new TaskLabelled(label, cost, childrenSet);
		if (childrenSet.size() > 0) {
			for (Task child : children) {
				child.setParent(t);
			}
		}
		return t;
	}
}
