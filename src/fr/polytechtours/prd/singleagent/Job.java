package fr.polytechtours.prd.singleagent;

import java.util.ArrayList;

public class Job implements Comparable<Job>{

	public int id;
	public int start;
	public int end;
	public ArrayList<Integer> consumes;
	public String belongTo;
	public int weight;
	public int factorOfSort;
	
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
		factorOfSort = maxConsume * (end - start);
	}


	@Override
	public int compareTo(Job job2) {
		int diffFactor = this.factorOfSort - job2.factorOfSort;
		if(diffFactor != 0){
			return diffFactor;
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
