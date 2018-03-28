package fr.polytechtours.prd.multiagent.model;

import java.util.ArrayList;

/**
 * 
 * Class machine to store the resources of plate-form
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Nov 30, 2017
 * 
 */
public class Machine {

	/**
	 * all resources with total quantity 
	 */
	public ArrayList<Integer> resources;
	
	/**
	 * constructor
	 */
	public Machine(){
		resources = new ArrayList<Integer>();
	}
}