package fr.polytechtours.prd.multiagent.exact;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.IAlgorithm;
import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class TestExact {

	public static void main(String[] args){
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		Data data = new Data();
		data.jobs.addAll((ArrayList<Job>) hashmap.get("jobs"));
		data.machine = (Machine)hashmap.get("machine");
		data.nbJobs = (int) hashmap.get("numJob");
		data.nbJobsA = (int) hashmap.get("numJobAgentA");
		data.epsilon = data.nbJobsA/2;
		data.weight = 0.1;
		data.agent = "A";
		
		EpsilonConstraint epsilonContraint = new EpsilonConstraint();
		epsilonContraint.loadParam(data);
		epsilonContraint.generateParetoFront();
		
		LinearCombination linearCombination = new LinearCombination();
		linearCombination.loadParam(data);
		linearCombination.generateParetoFront();
	}
}
