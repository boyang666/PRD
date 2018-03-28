package fr.polytechtours.prd.multiagent;

import fr.polytechtours.prd.multiagent.util.Commun;

/**
 * Main Class with main function
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Nov 20, 2018
 *
 */
public class Main {

	/**
	 * main function to create data and write into a file 
	 * @param args parameters
	 */
	public static void main(String[] args) {
		Commun.createRandomJobsAndResources(100, 3, 40, "instance-100-2-3-40.data");
	}

}
