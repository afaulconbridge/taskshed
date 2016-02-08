package jobshed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Demo {
    private static Logger log = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		Manager manager = new Manager();

		
		//create some grandchild tasks
		Task aaa = TaskLabelled.create("Aaa", 1.0f);
		Task aab = TaskLabelled.create("Aab", 1.0f);
		
		//create some child tasks
		Task aa = TaskLabelled.create("Aa", 1.0f, aaa, aab);
		Task ab = TaskLabelled.create("Ab", 1.0f);
		Task ac = TaskLabelled.create("Ac", 1.0f);

		Task ba = TaskLabelled.create("Ba", 1.0f);
		Task bb = TaskLabelled.create("Bb", 1.0f);
		Task bc = TaskLabelled.create("Bc", 1.0f);
		
		//create some tasks
		Task a = TaskLabelled.create("A", 1.0f, aa, ab, ac);
		Task b = TaskLabelled.create("B", 1.0f, ba, bb, bc);
		Task c = TaskLabelled.create("C", 1.0f);
		
		log.info("Created tasks");		
		
		manager.add(a);
		manager.add(b);
		manager.add(c);

		log.info("Added tasks");
		
		Agent agent = new Agent();
		manager.add(agent);
		
		log.info("Created agent(s)");
		
		log.info("Assign task(s) to agent(s) initially");		
		manager.assign();
		
		while (agent.getCurrentTask() != null) {
			
			log.info("Agent assigned: "+agent.getCurrentTask());
			
			//now the agent can complete the task
			manager.remove(agent.getCurrentTask());
			agent.setCurrentTask(null);
			log.info("Assign task(s) to agent(s)");
			
			manager.assign();
		
		}
		
		log.info("Agent assigned: "+agent.getCurrentTask());
	}

}
