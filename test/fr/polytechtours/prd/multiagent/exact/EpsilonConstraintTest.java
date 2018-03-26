package fr.polytechtours.prd.multiagent.exact;

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

/**
 * 
 * Test case to test epsilon constraint algorithm to verify if the implementation gives the correct solutions.<br>
 * One instance of test with 20 jobs where 40% jobs belong to agent A is used.<br>
 * The optimal pareto solutions are got and verified before:
 * <ul>
 * <li>A: 5, B: 10</li>
 * <li>A: 6, B: 9</li>
 * <li>A: 7, B: 8</li>
 * </ul>
 * 
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since 24 Mars, 2018
 *
 */
public class EpsilonConstraintTest {
	/**
	 * pareto front
	 */
	Set<ParetoSolution> front = new HashSet<ParetoSolution>();
	/**
	 * data to use
	 */
	Data data;
	/**
	 * optimal solutions verified
	 */
	int[][] optimalSolutions;

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
		
		EpsilonConstraint epsilonContraint = new EpsilonConstraint();
		epsilonContraint.loadParam(data);
		front = epsilonContraint.generateParetoFront();
		
		// optimal pareto solutions for this instance of data
		optimalSolutions = new int[3][2];
		optimalSolutions[0][0] = 5;
		optimalSolutions[0][1] = 10;
		
		optimalSolutions[1][0] = 6;
		optimalSolutions[1][1] = 9;
		
		optimalSolutions[2][0] = 7;
		optimalSolutions[2][1] = 8;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(3, front.size());
		
		Iterator<ParetoSolution> iter = front.iterator(); 
		while(iter.hasNext()){
			ParetoSolution solution = iter.next();
			boolean exist = false;
			for(int i=0; i<3; i++){
				if(solution.valueObjA == optimalSolutions[i][0] && solution.valueObjB == optimalSolutions[i][1]){
					exist = true;
					break;
				}
			}
			assertTrue(exist);
		}
	}

}
