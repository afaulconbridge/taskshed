package jobshed;

public class Agent {
	
	protected Task<?> currentTask = null;

	public Task<?> getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task<?> task) {
		if (task != null && task.isAssigned() && task.getAssignedTo() != this) {
			throw new IllegalArgumentException("Cannot work on assigned task");
		}
		this.currentTask = task;
		if (task != null && task.getAssignedTo() != this) {
			task.setAssignedTo(this);
		}
	}
	
	public boolean hasCurrentTask() {
		if (currentTask == null) {
			return false;
		} else {
			return true;
		}
	}

}
