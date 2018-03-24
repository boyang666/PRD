package fr.polytechtours.prd.multiagent.model;

import java.util.ArrayList;
import java.util.List;

public class ParetoSolution implements Comparable<ParetoSolution>{

	public List<Integer> sequence;
	
	public int valueObjA;
	
	public int valueObjB;
	
	public ParetoSolution(){
		this.sequence = new ArrayList<Integer>();
	}

	@Override
	public int compareTo(ParetoSolution solution) {
		if(this.valueObjA == solution.valueObjA && this.valueObjB == solution.valueObjB){
			return 0;
		}
		if(this.valueObjA < solution.valueObjA && this.valueObjB < solution.valueObjB){
			return -1;
		}
		else{
			return 1;
		}
	}
	
	@Override
	public int hashCode(){
		return this.valueObjA*100 + this.valueObjB;
	}
	
	@Override
	public boolean equals(Object solution){
		if(solution instanceof ParetoSolution && this.valueObjA == ((ParetoSolution)solution).valueObjA && this.valueObjB == ((ParetoSolution)solution).valueObjB){
			return true;
		}
		else{
			return false;
		}
	}
}
