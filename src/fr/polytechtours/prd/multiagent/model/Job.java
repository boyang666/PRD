package fr.polytechtours.prd.multiagent.model;

import java.util.ArrayList;

/**
 * 
 * class Job: all informations associated with a job
 * The start time, end time, a list of amounts for consuming the resources, 
 * which agent it belongs to, the weight and the factor for doing the sort
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Nov 30, 2017
 *
 */
public class Job implements Comparable<Job>{

	public static final int TYPE_FACTOR_SORT_SUM = 0;
	public static final int TYPE_FACTOR_SORT_MAX = 1;
	
	public int id; // id for the job
	public int start; // start time
	public int end; // end time
	public ArrayList<Integer> consumes; // list of consuming of resources
	public String belongTo; // which agent it belongs to
	public double weight; // the weight of job
	public double factorOfSort; // the factor for doing the sort
	
	/**
	 * constructor
	 */
	public Job(){
		consumes = new ArrayList<Integer>();
	}
	
	
	public void calculateFactorOfSort(int type) {
		if(type == Job.TYPE_FACTOR_SORT_SUM){
			int sumConsume = 0;
			for(int i=0; i<consumes.size(); i++){
				sumConsume += consumes.get(i); // sum of all consume
			}
			factorOfSort = sumConsume * (end - start);
		} else if(type == Job.TYPE_FACTOR_SORT_MAX){
			int maxConsume = consumes.get(0);
			for(int i=0; i<consumes.size(); i++){
				if(consumes.get(i) > maxConsume){ 
					maxConsume = consumes.get(i); // try to find the consume max
				}
			}
			factorOfSort = maxConsume * (end - start) / weight;
		}
		
		
	}



	@Override
	public int compareTo(Job job2) {
		double diffFactor = this.factorOfSort - job2.factorOfSort; // get the result of subtraction
		
		if(diffFactor != 0){ //if the factor of sort is not equal
			if(diffFactor < 0)
				return -1; // negative 
			else
				return 1; // positive
		}
		else{ // if the factor of sort is equal
			int diffEnd = this.end - job2.end; // subtraction of the end time
			if (diffEnd != 0){ // if the end time is different
				return diffEnd;
			}
			else { // if the end time is the same
				int diffStart = this.start - job2.start; //subtraction of the start time
				if(diffStart != 0){ // if the start time is different
					return diffStart;
				}
				else // if the start time is same
					return this.id - job2.id;
			}
		}
	}

	
}
