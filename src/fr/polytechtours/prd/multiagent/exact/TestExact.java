package fr.polytechtours.prd.multiagent.exact;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class TestExact {

	public static void main(String[] args){
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		@SuppressWarnings("unchecked")
		ArrayList<Job> jobs = (ArrayList<Job>) hashmap.get("jobs");
		Machine machine = (Machine)hashmap.get("machine");
		int nbJobs = (int) hashmap.get("numJob");
		int nbJobsA = (int) hashmap.get("numJobAgentA");
		
		EpsilonConstraint epsilonContraint = new EpsilonConstraint();
		
		for(int epsilon = nbJobsA/2; epsilon<=nbJobsA; epsilon++){
			HashMap<String, Object> result = epsilonContraint.execute(epsilon, jobs, machine, nbJobs, nbJobsA,"A");
			boolean solved = (boolean)result.get("solved");
			
			if(solved){//solve the symmetric problem
				
				int obj_v_B = (int)result.get("obj_value");
				
				HashMap<String, Object> result_sym = epsilonContraint.execute(obj_v_B, jobs, machine, nbJobs, nbJobs-nbJobsA, "B");
				int obj_v_A = (int)result_sym.get("obj_value");
				
				int[] solution = (int[]) result_sym.get("result_x");
				StringBuilder str = new StringBuilder("[");
				for(int i=0; i<solution.length; i++){
					str.append(solution[i]).append(",");
				}
				str.append("]");
				System.out.println(str.toString());
				
				System.out.println("Solution A = "+obj_v_A+" , Solution B = "+obj_v_B);
			}
			
			
		}
	}
}
