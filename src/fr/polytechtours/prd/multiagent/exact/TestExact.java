package fr.polytechtours.prd.multiagent.exact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.model.ParetoSolution;
import fr.polytechtours.prd.multiagent.util.Commun;
import fr.polytechtours.prd.multiagent.util.Timer;

/**
 * Test class for exact methods
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Feb 1, 2018
 *
 */
public class TestExact {

	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-20-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		Data data = new Data();
		data.jobs.addAll((ArrayList<Job>) hashmap.get("jobs"));
		data.machine = (Machine)hashmap.get("machine");
		data.nbJobs = (int) hashmap.get("numJob");
		data.nbJobsA = (int) hashmap.get("numJobAgentA");
		data.epsilon = data.nbJobsA/2;
		data.weight = 0.1;
		data.agent = "A";
		
		Timer timer = new Timer();
		timer.setStart(System.currentTimeMillis());
		EpsilonConstraint epsilonContraint = new EpsilonConstraint();
		epsilonContraint.loadParam(data);
		Set<ParetoSolution> front = epsilonContraint.generateParetoFront();
		timer.setEnd(System.currentTimeMillis());
		
		for(Iterator<ParetoSolution> iter=front.iterator(); iter.hasNext(); ){
			ParetoSolution solution = iter.next();
			StringBuilder builder = new StringBuilder("[");
			for(int i=0; i<solution.sequence.size(); i++){
				builder.append(solution.sequence.get(i)).append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append("]");
			System.out.println("------------------------------------------");
			System.out.println("Jobs to schedule: " + builder.toString());
			System.out.println("Number of jobs rejected of agent A: "+solution.valueObjA+" , "+"Number of jobs rejected of agent B: "+solution.valueObjB);
			System.out.println("------------------------------------------");
		}
		System.out.println("Time consumed: "+timer.calculateTimeConsume());
		
		/*LinearCombination linearCombination = new LinearCombination();
		linearCombination.loadParam(data);
		linearCombination.generateParetoFront();*/
	}
}
