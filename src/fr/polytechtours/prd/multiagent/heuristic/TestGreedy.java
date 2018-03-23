package fr.polytechtours.prd.multiagent.heuristic;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class TestGreedy {

	public static void main(String[] args){
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		Data data = new Data();
		data.jobs = (ArrayList<Job>) hashmap.get("jobs");
		data.machine = (Machine)hashmap.get("machine");
		data.nbJobs = (int) hashmap.get("numJob");
		data.nbJobsA = (int) hashmap.get("numJobAgentA");
		data.typeGreedy = Greedy.GREEDY_EPSILON;
		data.epsilon = data.nbJobsA/2;
		
		Greedy greedy = new Greedy();
		greedy.loadParam(data);
		greedy.generateParetoFront();
		
	}
}
