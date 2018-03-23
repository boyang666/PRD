package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;

import fr.polytechtours.prd.multiagent.model.Data;
/**
 * Individual to simulation one solution of NSGA2</br>
 * One individual contains :
 * <ul>
 * <li>genes: its genes of size N with 0 for scheduled and 1 for abandoned</li>
 * <li>fitness: values of objective functions</li>
 * <li>number dominated: the number of individuals which dominates this individual</li>
 * <li>set dominant: set of individuals which this individual dominates</li>
 * <li>crowdedDistance: crowded distance</li>
 * <li>valid: if this individual is feasible or not</li>
 * </ul>
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 6, 2018
 *
 */
public class Individual implements Calculate{
	
	/**
	 * data to use
	 */
	public Data data;
	/**
	 * genes of individual
	 */
	public ArrayList<Integer> genes;
	/**
	 * values of objective functions
	 */
	public ArrayList<Integer> valuesObj;
	/**
	 * number of individuals which dominate this individual
	 */
	public int numDominated;
	/**
	 * set of individuals which this individual dominates
	 */
	public ArrayList<Individual> setDominant;
	/**
	 * crowded distance
	 */
	public double crowdedDistance = 0.0;
	/**
	 * if this individual is feasible
	 */
	public boolean valide = true;
	/**
	 * constructor
	 */
	public Individual(Data data){
		this.data = data;
		genes = new ArrayList<Integer>();
		valuesObj = new ArrayList<Integer>();
		numDominated = 0;
		setDominant = new ArrayList<Individual>();
	}
	/**
	 * to change number dominated
	 * @param change
	 */
	public void changeNumDominated(int change){
		this.numDominated += change;
	}
	/**
	 * to add one individual into the set dominant
	 * @param ind
	 */
	public void addIndividualDominant(Individual ind){
		this.setDominant.add(ind);
	}

	@Override
	public void calculateValueObj() {
		valuesObj.clear();
		valuesObj.add(0);
		valuesObj.add(0);
		
		for(int i=0; i<data.nbJobs; i++){
			if(data.jobs.get(i).belongTo.equals("A")){
				if(genes.get(i) == 1){
					valuesObj.set(0, valuesObj.get(0)+1);
				}
			}
			else{
				if(genes.get(i) == 1){
					valuesObj.set(1, valuesObj.get(1)+1);
				}
			}
		}
	}

	@Override
	public void validate() {
		
		ArrayList<Integer> consumes = new ArrayList<Integer>();
		// to verify if the resources used are less than the total capacity
		for(int t=1; t<=data.maxEnd; t++){
			consumes.clear();
			for(int k=0; k<data.machine.resources.size(); k++){
				consumes.add(0);
			}
			for(int i=0; i<data.jobs.size(); i++){
				if(data.jobs.get(i).start <= t && data.jobs.get(i).end >= t && genes.get(i) == 0){
					for(int j=0; j<consumes.size(); j++){
						consumes.set(j, consumes.get(j) + data.jobs.get(i).consumes.get(j));
					}
				}
			}
			for(int iter=0; iter<consumes.size(); iter++){
				if(consumes.get(iter) > data.machine.resources.get(iter)){
					this.valide = false;
					break;
				}
			}
			if(!this.valide)break;
		}
	}
	
}
