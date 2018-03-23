package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class TestNSGA2 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		//Commun.createRandomJobsAndResources(100, 3, 40, "instance-100-2-3-40.data");
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		Data data = new Data();
		data.jobs = (ArrayList<Job>) hashmap.get("jobs");
		data.machine = (Machine)hashmap.get("machine");
		data.nbJobs = (int) hashmap.get("numJob");
		data.nbJobsA = (int) hashmap.get("numJobAgentA");
		data.maxEnd = Commun.getMaxEnd(data.jobs);
		//Collections.sort(Data.jobs);
		
		NSGA2 nsga2 = new NSGA2();
		nsga2.loadParam(data);
		nsga2.generateParetoFront();
		
	}
}
