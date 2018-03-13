package fr.polytechtours.prd.multiagent.heuristic.meta;

/**
 * Interface Calculate to provide calculate service 
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 1, 2018
 *
 */
public interface Calculate {

	/**
	 * calculate the value of function
	 */
	public void calculateValueObj();
	
	/**
	 * to verify if the current solution is feasible 
	 */
	public void validate();
}
