package fr.polytechtours.prd.multiagent.heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

import fr.polytechtours.prd.multiagent.IAlgorithm;
import fr.polytechtours.prd.multiagent.model.Data;
import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

/**
 * 
 * Greedy Heuristic for solving the multi-objectif problem.</br>
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
public class Greedy implements IAlgorithm{
	
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
	 * sort jobs as Epsilon Constraint algorithm</br>
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
	 * to execute the greedy as the linear combination
	 * @return hashmap with these elements:
	 * <ul>
	 * <li>key:solution, type of value:ArrayList<Job>, value:the jobs of class {@link Job} which are scheduled</li>
	 * </ul>
	 */
	public HashMap<String, Object> executeLinear(){
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		sortedJobs.addAll(sortLinear(data.jobs, data.weight));
		
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>();
		
		int maxTimeEnd = Commun.getMaxEnd(sortedJobs);
		int[][] resourceConsumed = new int[data.machine.resources.size()][maxTimeEnd];
		
		ArrayList<Job> jobSet = new ArrayList<Job>();
		jobSet.addAll(sortedJobs);
		
		while(!jobSet.isEmpty()){
			if(solution.isEmpty()){
				solution.add(jobSet.get(0));
				jobStack.push(jobSet.get(0));
				
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
				
				boolean flag = true;
				for(int i=0; i<data.machine.resources.size(); i++){
					if((resourceConsumed[i][jobSet.get(0).start] > data.machine.resources.get(i)) || (resourceConsumed[i][jobSet.get(0).end-1] > data.machine.resources.get(i))){
						flag = false;
					}
				}
				
				if(flag){
					
					int period = 0;
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
					
					if(period == jobSet.get(0).end - jobSet.get(0).start){
						solution.add(jobSet.get(0));
					}
					else{
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							for(int i=0; i<data.machine.resources.size(); i++){
								resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
							}
						}
						
						jobStack.pop();
					}
				}
				else{
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<data.machine.resources.size(); i++){
							resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
						}
					}
					
					jobStack.pop();
				}
			}
			
			jobSet.remove(0);
		}
		
		results.put("solution", solution);
		return results;
	}
	
	/**
	 * to execute the greedy as Epsilon Constraint algorithm</br>
	 * 
	 * @return hashmap with these elements:
	 * <ul>
	 * <li>key:solution, type of value:ArrayList<Job>, value:the jobs of class {@link Job} which are scheduled</li>
	 * </ul>
	 */
	public HashMap<String, Object> executeEpsilon(){
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		sortedJobs.addAll(sortEpsilon(data.jobs));
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>();
		
		int maxTimeEnd = Commun.getMaxEnd(sortedJobs);
		int[][] resourceConsumed = new int[data.machine.resources.size()][maxTimeEnd];
		
		ArrayList<Job> jobSet = new ArrayList<Job>();
		jobSet.addAll(sortedJobs);
		
		int nbScheduleA = 0;
		
		while(!jobSet.isEmpty()){
			if(jobSet.get(0).belongTo.equals("A") && nbScheduleA == (data.nbJobsA - data.epsilon)){
				jobSet.remove(0);
			}
			else {
				if(solution.isEmpty()){
					solution.add(jobSet.get(0));
					if(jobSet.get(0).belongTo.equals("A"))
						nbScheduleA++;
					jobStack.push(jobSet.get(0));
					
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
					
					boolean flag = true;
					for(int i=0; i<data.machine.resources.size(); i++){
						if((resourceConsumed[i][jobSet.get(0).start] > data.machine.resources.get(i)) || (resourceConsumed[i][jobSet.get(0).end-1] > data.machine.resources.get(i))){
							flag = false;
						}
					}
					
					if(flag){
						
						int period = 0;
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
						
						if(period == jobSet.get(0).end - jobSet.get(0).start){
							solution.add(jobSet.get(0));
							if(jobSet.get(0).belongTo.equals("A"));
								nbScheduleA++;
						}
						else{
							for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
								for(int i=0; i<data.machine.resources.size(); i++){
									resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
								}
							}
							
							jobStack.pop();
							
						}
					}
					else{
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							for(int i=0; i<data.machine.resources.size(); i++){
								resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
							}
						}
						
						jobStack.pop();
						
					}
				}
				
				jobSet.remove(0);
			}
		
		}
		
		results.put("solution", solution);
		return results;
		
	}

	@Override
	public void loadParam(Data data) {
		this.data = data;
	}

	@Override
	public HashMap<String, Object> execute() {
		if(data.typeGreedy == Greedy.GREEDY_LINEAR){
			return this.executeLinear();
		}
		return this.executeEpsilon();
	}

	@Override
	public void generateParetoFront() {
		data.typeGreedy = Greedy.GREEDY_EPSILON;
		for(data.epsilon = data.nbJobsA/2; data.epsilon <= data.nbJobs; data.epsilon++){
			this.loadParam(data);
			HashMap<String, Object> results = this.execute();
			ArrayList<Job> solution = new ArrayList<Job>();
			solution.addAll((ArrayList<Job>)results.get("solution"));
			int obj_v_A = 0;
			int obj_v_B = 0;
			StringBuilder str = new StringBuilder("[");
			for(int i=0; i<solution.size(); i++){
				str.append(solution.get(i).id).append(",");
				if(solution.get(i).id < data.nbJobsA)
					obj_v_A++;
				else
					obj_v_B++;
			}
			str.append("]");
			System.out.println(str.toString());
			System.out.println("Solution A : "+(data.nbJobsA - obj_v_A)+" , "+"Solution B : "+(data.nbJobs - data.nbJobsA - obj_v_B));
		}
		
	}
}
