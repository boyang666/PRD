package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.model.ParetoSolution;
import fr.polytechtours.prd.multiagent.util.Commun;
import fr.polytechtours.prd.multiagent.util.Timer;

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
		
		Timer timer = new Timer();
		timer.setStart(System.currentTimeMillis());
		NSGA2 nsga2 = new NSGA2();
		nsga2.loadParam(data);
		Set<ParetoSolution> front = nsga2.generateParetoFront();
		
		timer.setEnd(System.currentTimeMillis());
		
		for(Iterator<ParetoSolution> iter=front.iterator(); iter.hasNext(); ){
			ParetoSolution solution = iter.next();
			StringBuilder builder = new StringBuilder("[");
			for(int i=0; i<solution.sequence.size(); i++){
				builder.append(solution.sequence.get(i)).append(",");
			}
			builder.append("]");
			System.out.println(builder.toString());
			System.out.println("Solution A: "+solution.valueObjA+" , "+"Solution B: "+solution.valueObjB);
		}
		System.out.println("Time consumed: "+timer.calculateTimeConsume());
		
	}
}
