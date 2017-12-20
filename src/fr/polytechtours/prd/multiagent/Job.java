package fr.polytechtours.prd.multiagent;

import java.util.ArrayList;

public class Job implements Comparable<Job>{

	public int id;
	public int start;
	public int end;
	public ArrayList<Integer> consumes;
	public String belongTo;
	public double weight;
	public double factorOfSort;
	
	public Job(){
		consumes = new ArrayList<Integer>();
	}
	
	public void calculateFactorOfSort(){
		int maxConsume = consumes.get(0);
		for(int i=0; i<consumes.size(); i++){
			if(consumes.get(i) > maxConsume){
				maxConsume = consumes.get(i);
			}
		}
		/*int sumConsume = 0;
		for(int i=0; i<consumes.size(); i++){
			sumConsume += consumes.get(i);
		}*/
		//factorOfSort = sumConsume * (end - start);
		factorOfSort = maxConsume * (end - start) / weight;
	}


	@Override
	public int compareTo(Job job2) {
		double diffFactor = this.factorOfSort - job2.factorOfSort;
		if(diffFactor != 0){
			if(diffFactor < 0)
				return -1;
			else
				return 1;
		}
		else{
			int diffEnd = this.end - job2.end;
			if (diffEnd != 0){
				return diffEnd;
			}
			else {
				int diffStart = this.start - job2.start;
				if(diffStart != 0){
					return diffStart;
				}
				else
					return this.id - job2.id;
			}
		}
	}
}
