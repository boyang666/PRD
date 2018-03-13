package fr.polytechtours.prd.multiagent;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.exact.EpsilonContraint;
import fr.polytechtours.prd.multiagent.heuristic.Greedy;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class Main {

	public static void main(String[] args) {
		//Commun.createRandomJobsAndResources(100, 3, 40, "instance-100-2-3-40.data");
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		@SuppressWarnings("unchecked")
		ArrayList<Job> jobs = (ArrayList<Job>) hashmap.get("jobs");
		Machine machine = (Machine)hashmap.get("machine");
		int nbJobs = (int) hashmap.get("numJob");
		int nbJobsA = (int) hashmap.get("numJobAgentA");
		
		/*for(int i=0; i<jobs.size(); i++){
			System.out.println(jobs.get(i).id);
			System.out.println(jobs.get(i).belongTo);
		}*/
		
		//LinearConbination linear = new LinearConbination();
		//linear.execute();
		
		/*EpsilonContraint epsilonContraint = new EpsilonContraint();
		
		for(int epsilon = nbJobsA/2; epsilon<nbJobsA; epsilon++){
			HashMap<String, Object> result = epsilonContraint.execute(epsilon, jobs, machine, nbJobs, nbJobsA,"A");
			boolean solved = (boolean)result.get("solved");
			
			if(solved){//solve the symmetric problem
				
				int obj_v_B = (int)result.get("obj_value");
				
				HashMap<String, Object> result_sym = epsilonContraint.execute(obj_v_B, jobs, machine, nbJobs, nbJobs-nbJobsA, "B");
				int obj_v_A = (int)result_sym.get("obj_value");
				
				System.out.println("Solution A = "+obj_v_A+" , Solution B = "+obj_v_B);
			}
			
			
		}*/
		
		Greedy greedy = new Greedy();
		ArrayList<Job> sortedJobs = greedy.sortEpsilon(jobs);
		int epsilon = 38;
		ArrayList<Job> solution = greedy.executeEpsilon(sortedJobs, nbJobsA, machine, epsilon);
		int obj_v_A = 0;
		int obj_v_B = 0;
		for(int i=0; i<solution.size(); i++){
			if(solution.get(i).id < 40)
				obj_v_A++;
			else
				obj_v_B++;
		}
		System.out.println("Solution A : "+(nbJobsA - obj_v_A)+" , "+"Solution B : "+(nbJobs - nbJobsA - obj_v_B));
	}

}
