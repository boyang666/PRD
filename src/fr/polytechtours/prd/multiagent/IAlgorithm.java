package fr.polytechtours.prd.multiagent;

import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Data;

/**
 * 
 * Interface Algorithm give the common methods for executing the different algorithms<br>
 * To execute an algorithm, user should firstly load necessary parameters explicitly<br>
 * Then user could begin with executing the algorithm<br>
 * For certain methods, one object of data(with parameters) can give only one pareto solution<br>
 * In order to get the pareto front, we should always call for the method generateParetoFront.
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 23, 2018
 *
 */
public interface IAlgorithm {

	/**
	 * Load parameters by using one object data of type {@link Data}
	 * 
	 * @param data data object with different parameters
	 */
	public void loadParam(Data data);

	/**
	 * To execute the algorithm.<br>
	 * Before executing the algorithm, user should always load the parameters first.<br>
	 * This method returns only one Pareto solution for certain algorithms, in order to get the pareto front
	 * We should always call the method <strong>generateParetoFront()</strong> in Interface {@link IAlgorithm}
	 * @return important results got during the execution of algorithm
	 */
	public HashMap<String, Object> execute();
	
	/**
	 * To generate the pareto front with parameters adapted.<br>
	 * This method should be called after the loading of parameters with method <strong>loadParam</strong> in Interface {@link IAlgorithm}
	 */
	public void generateParetoFront();
}
