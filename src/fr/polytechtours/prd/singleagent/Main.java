package fr.polytechtours.prd.singleagent;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		
		Commun.createRandomJobsAndResources(20, 3, "instance-20-1-3.data");
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data = Commun.ReadDataFromFile("instance-20-1-3.data");
		ArrayList<Job> jobs = (ArrayList<Job>) data.get("jobs");
		Machine machine = (Machine) data.get("machine");
		Greedy greedy = new Greedy();
		ArrayList<Job> sortedJobs = greedy.sort(jobs);
		
		ArrayList<Job> solution = greedy.execute(sortedJobs, machine);
		System.out.println(solution.size());
		/*for(int i=0; i<solution.size(); i++){
			System.out.println(solution.get(i).id);
		}*/
	}

}
