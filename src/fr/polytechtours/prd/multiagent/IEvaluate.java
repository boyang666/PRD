package fr.polytechtours.prd.multiagent;

import java.util.Set;

import fr.polytechtours.prd.multiagent.model.ParetoSolution;

/**
 * Interface to evaluate a pareto front got by a heuristic algorithm.<br>
 * 
 * Different factors of evaluation are proposed:
 * <ul>
 * <li>Percentage of optimal solutions found</li>
 * <li>Mean distance between two pareto fronts</li>
 * <li>Hypervolume</li>
 * </ul>
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since 20 Mars, 2018
 *
 */
public interface IEvaluate {

	/**
	 * To calculate the mean distance between two pareto fronts.<br>
	 * In order to get the mean distance:
	 * <ol>
	 * <li>For each point on the heuristic front, we try to find the nearest point from the exact front.</li>
	 * <li>Calculate the distance of these two points and add to the total distance</li>
	 * <li>Calculate the mean of the distance.</li>
	 * </ol>
	 *  
	 * @param frontExact exact front got by an exact algorithm
	 * @return the mean distance between two fronts
	 */
	public double getMeanDistance(Set<ParetoSolution> frontExact);
	
	/**
	 * To calculate the percentage of optimal solutions found by the heuristic method
	 * 
	 * @param frontExact exact front got by an exact algorithm
	 * @return percentage of optimal solutions found
	 */
	public double percentOptimalSolution(Set<ParetoSolution> frontExact);
	
	/**
	 * To calculate the hypervolume between two pareto fronts<br>
	 * The hypervolume is the area between the two fronts which presents the interval of two fronts<br>
	 * The lower this hypervolume is, the better the performance of this heuristic is.
	 * 
	 * @param frontExact pareto front got by an exact algorithm
	 * @return the hypervolume between two fronts
	 */
	public double getHyperVolume(Set<ParetoSolution> frontExact);
}
