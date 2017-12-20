package fr.polytechtours.prd.singleagent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Greedy {
	
	public ArrayList<Job> sort(ArrayList<Job> jobs){
		ArrayList<Job> jobsGreedy = new ArrayList<Job>();
		jobsGreedy.addAll(jobs);
		Collections.sort(jobsGreedy);
		return jobsGreedy;
	}
	
	public int getMaxEnd(ArrayList<Job> jobs){
		int maxEnd = jobs.get(0).end;
		for(int i=0; i<jobs.size(); i++){
			if(jobs.get(i).end > maxEnd) {
				maxEnd = jobs.get(i).end;
			}
		}
		
		return maxEnd;
	}
	
	public ArrayList<Job> execute(ArrayList<Job> sortedJobs, Machine machine){
		ArrayList<Job> solution = new ArrayList<Job>();
		Stack<Job> jobStack = new Stack<Job>();
		
		int maxTimeEnd = this.getMaxEnd(sortedJobs);
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
	
	
}
