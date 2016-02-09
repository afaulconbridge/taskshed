package jobshed;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskTest {
    private Logger log = LoggerFactory.getLogger(TaskTest.class);
	
	@Test
	public void testIterator() {
		Manager manager = new Manager();
		
		//create some child tasks
		Task<String> aa = Task.create("Aa", 1.0f);
		Task<String> ab = Task.create("Ab", 1.0f);

		//create some tasks
		Task<String> a = Task.create("A", 1.0f, aa, ab);

		manager.add(a);
		
		Iterator<Task<?>> taskIterator = manager.getAvaliableTasks();
		
		List<Task<?>> taskList = new ArrayList<>();
		while (taskIterator.hasNext()) {
			log.info("fum");
			Task<?> task = taskIterator.next();
			log.info("Next task is: "+task);
			taskList.add(task);
		}
		
		assertEquals("must have 2 tasks in list", 2, taskList.size());
	}
}
