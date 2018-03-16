package fr.polytechtours.prd.multiagent.heuristic;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class TestGreedy {

	public static void main(String[] args){
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		@SuppressWarnings("unchecked")
		ArrayList<Job> jobs = (ArrayList<Job>) hashmap.get("jobs");
		Machine machine = (Machine)hashmap.get("machine");
		int nbJobs = (int) hashmap.get("numJob");
		int nbJobsA = (int) hashmap.get("numJobAgentA");
		
		Greedy greedy = new Greedy();
		ArrayList<Job> sortedJobs = greedy.sortEpsilon(jobs);
		for(int epsilon = nbJobsA/2; epsilon <= nbJobs; epsilon++){
			ArrayList<Job> solution = greedy.executeEpsilon(sortedJobs, nbJobsA, machine, epsilon);
			int obj_v_A = 0;
			int obj_v_B = 0;
			StringBuilder str = new StringBuilder("[");
			for(int i=0; i<solution.size(); i++){
				str.append(solution.get(i).id).append(",");
				if(solution.get(i).id < nbJobsA)
					obj_v_A++;
				else
					obj_v_B++;
			}
			str.append("]");
			System.out.println(str.toString());
			System.out.println("Solution A : "+(nbJobsA - obj_v_A)+" , "+"Solution B : "+(nbJobs - nbJobsA - obj_v_B));
		}
		
	}
}
