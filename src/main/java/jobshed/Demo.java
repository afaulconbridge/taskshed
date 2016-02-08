package jobshed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Demo {
    private static Logger log = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		Manager<String> manager = new Manager<>();

		
		//create some grandchild tasks
		Task<String> aaa = Task.create("Aaa", 1.0f);
		Task<String> aab = Task.create("Aab", 1.0f);
		
		//create some child tasks
		Task<String> aa = Task.create("Aa", 1.0f, aaa, aab);
		Task<String> ab = Task.create("Ab", 1.0f);
		Task<String> ac = Task.create("Ac", 1.0f);

		Task<String> ba = Task.create("Ba", 1.0f);
		Task<String> bb = Task.create("Bb", 1.0f);
		Task<String> bc = Task.create("Bc", 1.0f);
		
		//create some tasks
		Task<String> a = Task.create("A", 1.0f, aa, ab, ac);
		Task<String> b = Task.create("B", 1.0f, ba, bb, bc);
		Task<String> c = Task.create("C", 1.0f);
		
		log.info("Created tasks");		
		
		manager.add(a);
		manager.add(b);
		manager.add(c);

		log.info("Added tasks");
		
		Agent<String> agent = new Agent<String>();
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
