package fr.polytechtours.prd.multiagent.heuristic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.model.ParetoSolution;
import fr.polytechtours.prd.multiagent.util.Commun;

public class GreedyTest {
	
	Set<ParetoSolution> front = new HashSet<ParetoSolution>();
	
	Data data;


	@Before
	public void setUp() throws Exception {
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-20-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		
		data = new Data();
		data.jobs.addAll((ArrayList<Job>) hashmap.get("jobs"));
		data.machine = (Machine)hashmap.get("machine");
		data.nbJobs = (int) hashmap.get("numJob");
		data.nbJobsA = (int) hashmap.get("numJobAgentA");
		data.epsilon = data.nbJobsA/2;
		data.weight = 0.1;
		data.agent = "A";
		data.maxEnd = Commun.getMaxEnd(data.jobs);
		
		Greedy greedy = new Greedy();
		greedy.loadParam(data);
		front = greedy.generateParetoFront();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Iterator<ParetoSolution> iter = front.iterator();
		while(iter.hasNext()){
			ParetoSolution solution = iter.next();
			ArrayList<Integer> ids = (ArrayList<Integer>) solution.sequence;
			ArrayList<Job> jobsScheduled = new ArrayList<Job>();
			for(int i=0; i<ids.size(); i++){
				for(int j=0; j<data.jobs.size(); j++){
					if(data.jobs.get(j).id == ids.get(i)){
						jobsScheduled.add(data.jobs.get(j));
					}
				}
			}
			int[] consumes = new int[data.machine.resources.size()];
			
			boolean feasible = true;
			for(int t=0; t<data.maxEnd; t++){
				for(int m=0; m<consumes.length; m++){
					consumes[m] = 0;
				}
				for(int i=0; i<jobsScheduled.size(); i++){
					if(jobsScheduled.get(i).start <= t && jobsScheduled.get(i).end >= t){
						for(int k=0; k<consumes.length; k++){
							consumes[k] += jobsScheduled.get(i).consumes.get(k);
						}
					}
				}
				for(int n=0; n<consumes.length; n++){
					if(consumes[n] > data.machine.resources.get(n)){
						feasible = false;
					}
				}
				if(!feasible){
					break;
				}
			}
			
			assertTrue(feasible);
		}
	}

}
