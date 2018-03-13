package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;

/**
 * This class provides methods to calculate the crowding distances for individuals
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 1, 2018
 *
 */
public class CrowdingDistanceAssignment {
	
	/**
	 * to calculate the crowding distances for each individual
	 * @param pop the population
	 */
	public static void distanceCalculator(ArrayList<Individual> pop){

		// initiation of crowding distances
		int size = pop.size();
		for(int i=0; i<size; i++){
			pop.get(i).crowdedDistance = 0.0;
		}
		
		//to calculate crowding distances for each individual
		for(int j=0; j< Constant.NUM_OBJ; j++){
			pop = sort(pop, j);//to sort the individuals in order of their fitness
			pop.get(0).crowdedDistance = pop.get(pop.size() - 1).crowdedDistance = Constant.MAX_INT;// the first and the last with the max value
			
			for(int k=1; k<pop.size() - 2; k++){
				if(maxFunctionObj(pop, j) == minFunctionObj(pop, j))
					pop.get(k).crowdedDistance = Constant.MAX_INT;
				else
					pop.get(k).crowdedDistance = pop.get(k).crowdedDistance + 
						(pop.get(k+1).valuesObj.get(j) - pop.get(k-1).valuesObj.get(j)) / (maxFunctionObj(pop, j) - minFunctionObj(pop, j));
			}
		}
	} 
	
	/**
	 * to sort the individuals in order of their fitness</br>
	 * the fitness if calculated by the objective function 
	 * @param popNonSorted the population not sorted
	 * @param numFunctionObj index of objective function
	 * @return list of individuals sorted
	 */
	public static ArrayList<Individual> sort(ArrayList<Individual> popNonSorted, int numFunctionObj) {

		Individual temp;
		ArrayList<Individual> pop = new ArrayList<Individual>();
		pop.addAll(popNonSorted);
		for(int i=0;i<pop.size()-1;i++){
			for(int j=0;j<pop.size()-1-i;j++){
				if(pop.get(j).valuesObj.get(numFunctionObj) > pop.get(j+1).valuesObj.get(numFunctionObj)){
					temp = pop.get(j);
					pop.set(j,pop.get(j+1));
					pop.set(j+1, temp);
				}
			}
		}
		
		return pop;
	}
	
	/**
	 * Sort all individuals by their crowding distances in descent order
	 * @param popNonSorted population not sorted
	 * @return list of individuals sorted
	 */
	public static ArrayList<Individual> sortByDistance(ArrayList<Individual> popNonSorted){
		Individual temp;
		ArrayList<Individual> pop = new ArrayList<Individual>();
		pop.addAll(popNonSorted);
		for(int i=0;i<pop.size()-1;i++){
			for(int j=0;j<pop.size()-1-i;j++){
				if(pop.get(j).crowdedDistance < pop.get(j+1).crowdedDistance){
					temp = pop.get(j);
					pop.set(j,pop.get(j+1));
					pop.set(j+1, temp);
				}
			}
		}
		
		return pop;
	}
	
	/**
	 * Get the max value of the objective function
	 * @param pop population
	 * @param numFunctionObj index of objective function
	 * @return max value
	 */
	public static int maxFunctionObj(ArrayList<Individual> pop, int numFunctionObj){
		int max = -1;
		for(int i=0; i<pop.size(); i++){
			if(pop.get(i).valuesObj.get(numFunctionObj) > max){
				max = pop.get(i).valuesObj.get(numFunctionObj);
			}
		}
		return max;
	}
	
	/**
	 * Get the min value of the objective function
	 * @param pop population
	 * @param numFunctionObj index of objective function
	 * @return min value
	 */
	public static int minFunctionObj(ArrayList<Individual> pop, int numFunctionObj){
		int min = Constant.MAX_INT;
		for(int i=0; i<pop.size(); i++){
			if(pop.get(i).valuesObj.get(numFunctionObj) < min){
				min = pop.get(i).valuesObj.get(numFunctionObj);
			}
		}
		return min;
	}

} 
