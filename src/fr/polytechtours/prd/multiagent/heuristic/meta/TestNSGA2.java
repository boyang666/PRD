package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class TestNSGA2 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		//Commun.createRandomJobsAndResources(100, 3, 40, "instance-100-2-3-40.data");
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		Data.jobs = (ArrayList<Job>) hashmap.get("jobs");
		Data.machine = (Machine)hashmap.get("machine");
		Data.nbJobs = (int) hashmap.get("numJob");
		Data.nbJobsA = (int) hashmap.get("numJobAgentA");
		Data.maxEnd = Commun.getMaxEnd(Data.jobs);
		
		NSGA2 nsga2 = new NSGA2();
		ArrayList<Individual> result = nsga2.execute();
		for(int i=0; i<result.size(); i++){
			System.out.println("result A: "+result.get(i).valuesObj.get(0)+" , B:"+result.get(i).valuesObj.get(1));
		}
	}
}
