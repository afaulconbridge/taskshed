package jobshed;

public class Demo {

	public static void main(String[] args) {
		Manager manager = new Manager();
		
		//create some tasks
		Task a = new TaskLabelled("A", 1.0f);
		Task b = new TaskLabelled("B", 1.0f);
		Task c = new TaskLabelled("C", 1.0f);

		
		//create some child tasks
		Task aa = new TaskLabelled("Aa", 1.0f, a);
		Task ab = new TaskLabelled("Ab", 1.0f, a);
		Task ac = new TaskLabelled("Ac", 1.0f, a);

		Task ba = new TaskLabelled("Ba", 1.0f, b);
		Task bb = new TaskLabelled("Bb", 1.0f, b);
		Task bc = new TaskLabelled("Bc", 1.0f, b);
		
		//create some grandchild tasks
		Task aaa = new TaskLabelled("Aaa", 1.0f, aa);
		Task aab = new TaskLabelled("Aab", 1.0f, aa);
		
		manager.add(a);
		manager.add(b);
		manager.add(c);
		
		
	}

}
