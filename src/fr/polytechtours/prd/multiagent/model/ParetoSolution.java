package fr.polytechtours.prd.multiagent.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is defined as the representation of one pareto solution<br>
 * Three elements are included in this class:
 * <ul>
 * <li>sequence: list of ids of jobs which are able to be scheduled </li>
 * <li>valueObjA: the value of the objective function for agent A </li>
 * <li>valueObjB: the value of the objective function for agent B </li>
 * </ul>
 * 
 * The order of domination is also defined by implementing the interface {@link Comparable}<br>
 * If one pareto solution dominates another one, the method compareTo return -1<br>
 * 
 * <p>
 * hashCode() and equals() are overrided. <br>
 * We note that two pareto solution are the same when they have the same values for each objective function
 * </p>
 * 
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since 24 Mars, 2018
 *
 */
public class ParetoSolution implements Comparable<ParetoSolution>{
	/**
	 * sequence of ids of jobs
	 */
	public List<Integer> sequence;
	/**
	 * value of objective function for agent A
	 */
	public int valueObjA;
	/**
	 * value of objective function for agent B
	 */
	public int valueObjB;
	/**
	 * constructor
	 */
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
