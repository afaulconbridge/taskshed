package jobshed;

public class TaskLabelled extends Task {

	private final String label;

	public TaskLabelled(String label, float cost) {
		super(cost);
		this.label = label;
	}

	public TaskLabelled(String label, float cost, Task parent) {
		super(cost, parent);
		this.label = label;
	}
	
	
}
