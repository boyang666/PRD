package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;

/**
 * Data to use for testing the NSGA2
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 5, 2018
 *
 */
public class Data {
	/**
	 * list of jobs
	 */
	public static ArrayList<Job> jobs;
	/**
	 * machine with resources
	 */
	public static Machine machine;
	/**
	 * number of jobs
	 */
	public static int nbJobs = 0;
	/**
	 * number of jobs of agent A
	 */
	public static int nbJobsA = 0;
	/**
	 * max end time of all jobs
	 */
	public static int maxEnd = 0;
}
