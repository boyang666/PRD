package fr.polytechtours.prd.multiagent.heuristic.meta;

/**
 * All constants as configuration of NSGA2 
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 1, 2018
 *
 */
public class Constant {

	/**
	 * number of objective function
	 */
	public static final int NUM_OBJ = 2;
	
	/**
	 * max int
	 */
	public static final int MAX_INT = 65535;
	
	/**
	 * number of iteration
	 */
	public static final int NUM_ITERATION = 500;
	
	/**
	 * size of population
	 */
	public static final int SIZE_POPULATION = 100;
	
	/**
	 * probability of crossover
	 */
	public static final double PROB_CROSSOVER = 0.8;
	
	/**
	 * probability of mutation to happen
	 */
	public static final double PROB_MUTATION = 0.8;
}
