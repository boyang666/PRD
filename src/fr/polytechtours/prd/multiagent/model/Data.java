package fr.polytechtours.prd.multiagent.model;

import java.util.ArrayList;

import fr.polytechtours.prd.multiagent.heuristic.Greedy;

/**
 * Data to use for parameters<br>
 * In order to execute the algorithms, user should create this object by initializing all necessary parameters
 * 
 * @author Boyang Wang
 * @version 2.0
 * @since Mars 23, 2018
 *
 */
public class Data {
	/**
	 * list of jobs
	 */
	public ArrayList<Job> jobs = new ArrayList<Job>();
	/**
	 * machine with resources
	 */
	public Machine machine;
	/**
	 * number of jobs
	 */
	public int nbJobs = 0;
	/**
	 * number of jobs of agent A
	 */
	public int nbJobsA = 0;
	/**
	 * max end time of all jobs
	 */
	public int maxEnd = 0;
	/**
	 * agent label
	 */
	public String agent = "A";
	/**
	 * value of epsilon used by epsilon constraint
	 */
	public int epsilon = 0;
	/**
	 * value of weight used by linear combination
	 */
	public double weight = 0.0;
	/**
	 * type of greedy algorithm
	 */
	public int typeGreedy = Greedy.GREEDY_EPSILON;
}
