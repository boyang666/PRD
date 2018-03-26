package fr.polytechtours.prd.multiagent.heuristic;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import fr.polytechtours.prd.multiagent.IAlgorithm;
import fr.polytechtours.prd.multiagent.IEvaluate;
import fr.polytechtours.prd.multiagent.exact.EpsilonConstraint;
import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.model.ParetoSolution;
import fr.polytechtours.prd.multiagent.util.Commun;

/**
 * 
 * Greedy Heuristic for solving the multi-objectif problem.<br>
 * Two types of execution are implemented:
 * <ol>
 * <li>to execute the greedy algorithm as the linear combination</li>
 * <li>to execute the greedy algorithm as the epsilon constraint</li>
 * </ol>
 * User is responsible to choose one of the two types to run the algorithm.
 * 
 * @author Boyang Wang
 * @version 2.0
 * @since Mars 10, 2018
 *
 */
public class Greedy implements IAlgorithm, IEvaluate{
	
	/**
	 * type defined to perform the greedy as linear combination algorithm
	 */
	public static int GREEDY_LINEAR = 0;
	/**
	 * type defined to perform the greedy as epsilon constraint algorithm
	 */
	public static int GREEDY_EPSILON = 1;
	/**
	 * data object with parameters
	 */
	public Data data;
	/**
	 * pareto front
	 */
	public Set<ParetoSolution> paretoFront;

	/**
	 * sort the jobs as linear combination algorithm
	 * 
	 * @param jobs list of jobs
	 * @param lambda weight of jobs of agent A
	 * @return list of jobs sorted
	 */
	private ArrayList<Job> sortLinear(ArrayList<Job> jobs, double lambda){
		ArrayList<Job> jobsGreedy = new ArrayList<Job>();
		jobsGreedy.addAll(jobs);
		for(int i=0; i<jobsGreedy.size(); i++){
			if(jobsGreedy.get(i).belongTo.equals("A"))
				jobsGreedy.get(i).weight = lambda;
			else
				jobsGreedy.get(i).weight = 1 - lambda;
		}
		Collections.sort(jobsGreedy);
		return jobsGreedy;
	}
	
	/**
	 * sort jobs as Epsilon Constraint algorithm<br>
	 * For each agent, their jobs are sorted just among their range
	 * @param jobs list of jobs
	 * @return jobs sorted
	 */
	private ArrayList<Job> sortEpsilon(ArrayList<Job> jobs){
		ArrayList<Job> jobsGreedy = new ArrayList<Job>();
		jobsGreedy.addAll(jobs);
		ArrayList<Job> jobsASorted = new ArrayList<Job>();
		ArrayList<Job> jobsBSorted = new ArrayList<Job>();
		
		for(int i=0; i<jobsGreedy.size(); i++){
			if(jobsGreedy.get(i).belongTo.equals("A"))
				jobsASorted.add(jobsGreedy.get(i));
			else
				jobsBSorted.add(jobsGreedy.get(i));
		}
		
		Collections.sort(jobsASorted);
		Collections.sort(jobsBSorted);
		jobsGreedy.clear();
		jobsGreedy.addAll(jobsASorted);
		jobsGreedy.addAll(jobsBSorted);
		
		return jobsGreedy;
	}
	
	/**
	 * to execute the greedy as the linear combination:
	 * <ol>
	 * <li>Sort all jobs by using <strong>sortLinear</strong>.</li>
	 * <li>Choose jobs in order to schedule when the constraints are respected. If not feasible, do not schedule this job.</li>
	 * <li>Return the solution</li>
	 * </ol>
	 * @return hashmap with these elements:
	 * <ul>
	 * <li>key:solution, type of value:ArrayList of {@link Job}, value:the jobs of class {@link Job} which are scheduled</li>
	 * </ul>
	 */
	public HashMap<String, Object> executeLinear(){
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		sortedJobs.addAll(sortLinear(data.jobs, data.weight));
		
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>(); // jobs to schedule of every moment
		
		int maxTimeEnd = Commun.getMaxEnd(sortedJobs);
		int[][] resourceConsumed = new int[data.machine.resources.size()][maxTimeEnd]; // resources consumed of all moment
		
		ArrayList<Job> jobSet = new ArrayList<Job>();
		jobSet.addAll(sortedJobs); // all jobs existing
		
		while(!jobSet.isEmpty()){ // when there are jobs not evaluated
			if(solution.isEmpty()){ // add a job to solution from the set when the solution is empty
				solution.add(jobSet.get(0));
				jobStack.push(jobSet.get(0)); // push to the stack of jobs
				
				// calculate the resources consumed
				for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
					for(int i=0; i<data.machine.resources.size(); i++){
						resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
					}
				}

			}
			else{
				jobStack.push(jobSet.get(0));
				for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
					for(int i=0; i<data.machine.resources.size(); i++){
						resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
					}
				}
				
				// verify if the resources consumed are less than the total quantity at the moment of addition
				boolean flag = true;
				for(int i=0; i<data.machine.resources.size(); i++){
					if((resourceConsumed[i][jobSet.get(0).start] > data.machine.resources.get(i)) || (resourceConsumed[i][jobSet.get(0).end-1] > data.machine.resources.get(i))){
						flag = false; // not feasible
					}
				}
				
				// if addition of the current job is feasible
				if(flag){
					
					int period = 0;
					
					// verify the feasibility of all period of the execution of these jobs 
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						
						boolean flagT = true;
						for(int i=0; i<data.machine.resources.size(); i++){
							if(resourceConsumed[i][t] > data.machine.resources.get(i)){
								flagT = false;
							}
						}
						
						if(flagT){
							period++;
						}
					}
					
					// if feasible during all period, add current job into the solution
					if(period == jobSet.get(0).end - jobSet.get(0).start){
						solution.add(jobSet.get(0));
					}
					else{ // if not, roll back and remove the current job from the stack
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							for(int i=0; i<data.machine.resources.size(); i++){
								resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
							}
						}
						
						jobStack.pop();
					}
				}
				else{ // if addition of the current job is not feasible
					// roll back
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<data.machine.resources.size(); i++){
							resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
						}
					}
					
					jobStack.pop();
				}
			}
			
			// end of evaluation of the current job, remove from set
			jobSet.remove(0);
		}
		
		results.put("solution", solution);
		return results;
	}
	
	/**
	 * to execute the greedy as Epsilon Constraint algorithm<br>
	 * <ol>
	 * <li>Sort all jobs by using <strong>sortEpsilon</strong>.</li>
	 * <li>Choose jobs in order to schedule when the constraints are respected. If not feasible, do not schedule this job.</li>
	 * <li>Return the solution</li>
	 * </ol>
	 * @return hashmap with these elements:
	 * <ul>
	 * <li>key:solution, type of value:ArrayList of {@link Job}, value:the jobs of class {@link Job} which are scheduled</li>
	 * </ul>
	 */
	public HashMap<String, Object> executeEpsilon(){
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		sortedJobs.addAll(sortEpsilon(data.jobs));
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>(); // jobs to schedule of every moment
		
		int maxTimeEnd = Commun.getMaxEnd(sortedJobs);
		int[][] resourceConsumed = new int[data.machine.resources.size()][maxTimeEnd]; // resources consumed of all moment
		
		ArrayList<Job> jobSet = new ArrayList<Job>(); // all jobs existing not evaluated
		jobSet.addAll(sortedJobs);
		
		int nbScheduleA = 0; // number of jobs of agent A scheduled
		
		while(!jobSet.isEmpty()){ // when there are jobs not evaluated
			// when the number of jobs of agent A scheduled equals to the number of jobs - epsilon
			if(jobSet.get(0).belongTo.equals("A") && nbScheduleA == (data.nbJobsA - data.epsilon)){
				jobSet.remove(0); // remove the current job and do not evaluate it
			}
			else {
				if(solution.isEmpty()){ // add a job to solution from the set when the solution is empty
					solution.add(jobSet.get(0));
					if(jobSet.get(0).belongTo.equals("A"))
						nbScheduleA++;
					jobStack.push(jobSet.get(0));
					
					// calculate the resources consumed
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<data.machine.resources.size(); i++){
							resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
						}
					}

				}
				else{
					jobStack.push(jobSet.get(0));
					
					// calculate the resources consumed
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<data.machine.resources.size(); i++){
							resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
						}
					}
					
					// verify if the resources consumed are less than the total quantity at the moment of addition
					boolean flag = true;
					for(int i=0; i<data.machine.resources.size(); i++){
						if((resourceConsumed[i][jobSet.get(0).start] > data.machine.resources.get(i)) || (resourceConsumed[i][jobSet.get(0).end-1] > data.machine.resources.get(i))){
							flag = false;
						}
					}
					
					if(flag){ // if addition of the current job is feasible
						
						int period = 0;
						
						// verify the feasibility of all period of the execution of these jobs 
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							
							boolean flagT = true;
							for(int i=0; i<data.machine.resources.size(); i++){
								if(resourceConsumed[i][t] > data.machine.resources.get(i)){
									flagT = false;
								}
							}
							
							if(flagT){
								period++;
							}
						}
						
						// if feasible during all period, add current job into the solution
						if(period == jobSet.get(0).end - jobSet.get(0).start){
							solution.add(jobSet.get(0));
							if(jobSet.get(0).belongTo.equals("A")); // if the current job belongs to agent A
								nbScheduleA++;
						}
						else{ // if not, roll back and remove the current job from the stack
							for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
								for(int i=0; i<data.machine.resources.size(); i++){
									resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
								}
							}
							
							jobStack.pop();
							
						}
					}
					else{ // if not, roll back and remove the current job from the stack
						// rol back
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							for(int i=0; i<data.machine.resources.size(); i++){
								resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
							}
						}
						
						jobStack.pop();
						
					}
				}
				
				jobSet.remove(0); // end of evaluation of the current job, remove from set
			}
		
		}
		
		results.put("solution", solution);
		return results;
		
	}

	@Override
	public void loadParam(Data data) {
		this.data = new Data();
		this.data.jobs = data.jobs;
		this.data.machine = data.machine;
		this.data.nbJobs = data.nbJobs;
		this.data.nbJobsA = data.nbJobsA;
		this.data.typeGreedy = data.typeGreedy;
		this.data.epsilon = data.epsilon;
	}

	@Override
	public HashMap<String, Object> execute() {
		if(data.typeGreedy == Greedy.GREEDY_LINEAR){
			return this.executeLinear();
		}
		return this.executeEpsilon();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<ParetoSolution> generateParetoFront() {
		paretoFront = new HashSet<ParetoSolution>();
		data.typeGreedy = Greedy.GREEDY_EPSILON;
		for(data.epsilon = data.nbJobsA/2; data.epsilon <= data.nbJobs; data.epsilon++){ // epsilon starts from (number of jobs of agent A / 2)
			this.loadParam(data);
			HashMap<String, Object> results = this.execute();
			ArrayList<Job> solution = new ArrayList<Job>();
			solution.addAll((ArrayList<Job>)results.get("solution"));
			int obj_v_A = 0;
			int obj_v_B = 0;
			ParetoSolution paretoSolution = new ParetoSolution();
			for(int i=0; i<solution.size(); i++){
				paretoSolution.sequence.add(solution.get(i).id);
				if(solution.get(i).id < data.nbJobsA)
					obj_v_A++;
				else
					obj_v_B++;
			}
			paretoSolution.valueObjA = (data.nbJobsA - obj_v_A);
			paretoSolution.valueObjB = (data.nbJobs - data.nbJobsA - obj_v_B);
			paretoFront.add(paretoSolution);
		}
		return paretoFront;
	}

	@Override
	public double getMeanDistance(Set<ParetoSolution> frontExact) {
		HashSet<ParetoSolution> frontGreedy = (HashSet<ParetoSolution>) this.paretoFront;
		
		// initiation of iterators
		Iterator<ParetoSolution> iterExact; 
		Iterator<ParetoSolution> iterGreedy = frontGreedy.iterator();
		double distanceTotal = 0.0;
		
		while(iterGreedy.hasNext()){
			ParetoSolution solutionGreedy = iterGreedy.next();
			double minDistance = Double.MAX_VALUE; // min distance
			iterExact = frontExact.iterator(); 
			while(iterExact.hasNext()){
				ParetoSolution solutionExact = iterExact.next();
				double distanceTemp = Math.sqrt(Math.pow((solutionGreedy.valueObjA - solutionExact.valueObjA), 2) + Math.pow((solutionGreedy.valueObjB - solutionExact.valueObjB), 2));
				if(distanceTemp < minDistance){ // if the temp distance is smaller
					minDistance = distanceTemp;
				}
			}
			
			distanceTotal += minDistance;
		}
		
		return (double)(distanceTotal / frontGreedy.size());
		
	}

	@Override
	public double percentOptimalSolution(Set<ParetoSolution> frontExact) {

		HashSet<ParetoSolution> frontGreedy = (HashSet<ParetoSolution>) this.paretoFront;
		
		double numOptimal = 0.0;
		// initiation of iterators
		Iterator<ParetoSolution> iterExact = frontExact.iterator(); 
		Iterator<ParetoSolution> iterGreedy;
		
		while(iterExact.hasNext()){
			ParetoSolution solutionExact = iterExact.next();
			iterGreedy = frontGreedy.iterator();
			while(iterGreedy.hasNext()){
				ParetoSolution solutionGreedy = iterGreedy.next();
				// if the solution is the same
				if(solutionGreedy.valueObjA == solutionExact.valueObjA && solutionGreedy.valueObjB == solutionExact.valueObjB){
					numOptimal++;
					break;
				}
			}
		}
		
		return (double)(numOptimal * 100 / frontExact.size());
	}
}
