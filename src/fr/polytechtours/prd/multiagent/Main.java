package fr.polytechtours.prd.multiagent;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

	public static void main(String[] args) {
		//Commun.createRandomJobsAndResources(100, 3, 40, "instance-100-2-3-40.data");
		//HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-20-2-3-30.data");
		//ArrayList<Job> jobs = (ArrayList<Job>) hashmap.get("jobs");
		/*for(int i=0; i<jobs.size(); i++){
			System.out.println(jobs.get(i).id);
			System.out.println(jobs.get(i).belongTo);
		}*/
		
		//LinearConbination linear = new LinearConbination();
		//linear.execute();
		
		EpsilonContraint epsilonContraint = new EpsilonContraint();
		epsilonContraint.execute(10);
		
		/*HashMap<String, Object> data = Commun.ReadDataFromFile("instance-100-2-3-40.data");
		ArrayList<Job> jobs = (ArrayList<Job>) data.get("jobs");
		Machine machine = (Machine) data.get("machine");
		int nbJobs = (int) data.get("numJob");
		int nbJobsA = (int) data.get("numJobAgentA");
		Greedy greedy = new Greedy();
		ArrayList<Job> sortedJobs = greedy.sortEpsilon(jobs);
		int epsilon = 26;
		ArrayList<Job> solution = greedy.executeEpsilon(sortedJobs, nbJobsA, machine, epsilon);
		for(int i=0; i<solution.size(); i++){
			System.out.println(solution.get(i).id);
		}*/
	}

}
