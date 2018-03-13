package fr.polytechtours.prd.multiagent.heuristic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;


public class Greedy {

	public ArrayList<Job> sortLinear(ArrayList<Job> jobs, double lambda){
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
	
	public ArrayList<Job> sortEpsilon(ArrayList<Job> jobs){
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
	
	
	public ArrayList<Job> executeLinear(ArrayList<Job> sortedJobs, Machine machine){
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>();
		
		int maxTimeEnd = Commun.getMaxEnd(sortedJobs);
		int[][] resourceConsumed = new int[machine.resources.size()][maxTimeEnd];
		
		ArrayList<Job> jobSet = new ArrayList<Job>();
		jobSet.addAll(sortedJobs);
		
		while(!jobSet.isEmpty()){
			if(solution.isEmpty()){
				solution.add(jobSet.get(0));
				jobStack.push(jobSet.get(0));
				
				for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
					for(int i=0; i<machine.resources.size(); i++){
						resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
					}
				}

			}
			else{
				jobStack.push(jobSet.get(0));
				for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
					for(int i=0; i<machine.resources.size(); i++){
						resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
					}
				}
				
				boolean flag = true;
				for(int i=0; i<machine.resources.size(); i++){
					if((resourceConsumed[i][jobSet.get(0).start] > machine.resources.get(i)) || (resourceConsumed[i][jobSet.get(0).end-1] > machine.resources.get(i))){
						flag = false;
					}
				}
				
				if(flag){
					
					int period = 0;
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						
						boolean flagT = true;
						for(int i=0; i<machine.resources.size(); i++){
							if(resourceConsumed[i][t] > machine.resources.get(i)){
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
							for(int i=0; i<machine.resources.size(); i++){
								resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
							}
						}
						
						jobStack.pop();
					}
				}
				else{
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<machine.resources.size(); i++){
							resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
						}
					}
					
					jobStack.pop();
				}
			}
			
			jobSet.remove(0);
		}
		
		return solution;
	}
	
	public ArrayList<Job> executeEpsilon(ArrayList<Job> sortedJobs, int nbJobsA, Machine machine, int epsilon){
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>();
		
		int maxTimeEnd = Commun.getMaxEnd(sortedJobs);
		int[][] resourceConsumed = new int[machine.resources.size()][maxTimeEnd];
		
		ArrayList<Job> jobSet = new ArrayList<Job>();
		jobSet.addAll(sortedJobs);
		
		int nbScheduleA = 0;
		
		while(!jobSet.isEmpty()){
			if(jobSet.get(0).belongTo.equals("A") && nbScheduleA == (nbJobsA - epsilon)){
				jobSet.remove(0);
			}
			else {
				if(solution.isEmpty()){
					solution.add(jobSet.get(0));
					if(jobSet.get(0).belongTo.equals("A"))
						nbScheduleA++;
					jobStack.push(jobSet.get(0));
					
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<machine.resources.size(); i++){
							resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
						}
					}

				}
				else{
					jobStack.push(jobSet.get(0));
					for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
						for(int i=0; i<machine.resources.size(); i++){
							resourceConsumed[i][t] += jobSet.get(0).consumes.get(i);
						}
					}
					
					boolean flag = true;
					for(int i=0; i<machine.resources.size(); i++){
						if((resourceConsumed[i][jobSet.get(0).start] > machine.resources.get(i)) || (resourceConsumed[i][jobSet.get(0).end-1] > machine.resources.get(i))){
							flag = false;
						}
					}
					
					if(flag){
						
						int period = 0;
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							
							boolean flagT = true;
							for(int i=0; i<machine.resources.size(); i++){
								if(resourceConsumed[i][t] > machine.resources.get(i)){
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
								for(int i=0; i<machine.resources.size(); i++){
									resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
								}
							}
							
							jobStack.pop();
							
						}
					}
					else{
						for(int t=jobSet.get(0).start; t<jobSet.get(0).end; t++){
							for(int i=0; i<machine.resources.size(); i++){
								resourceConsumed[i][t] -= jobSet.get(0).consumes.get(i);
							}
						}
						
						jobStack.pop();
						
					}
				}
				
				jobSet.remove(0);
			}
		
		}
		
		return solution;
		
	}
}
