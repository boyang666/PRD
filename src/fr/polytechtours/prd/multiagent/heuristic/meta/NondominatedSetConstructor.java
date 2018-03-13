package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;

/**
 * This class provides methods to sort the population by their level of dominant</br>
 * In return, several sets are returned in order of their rank of dominant</br>
 * The lower rank comes first
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 8, 2018
 *
 */
public class NondominatedSetConstructor {

	/**
	 * To verify if individual p dominates q.</br>
	 * If p dominates q, all values of objective functions of p are smaller than q
	 * @param p individual
	 * @param q individual
	 * @return true if p dominates q, false if not
	 */
	public static Boolean pDomQ(Individual p, Individual q){
		
		ArrayList<Integer> pElems = p.valuesObj;
		ArrayList<Integer> qElems = q.valuesObj;
		for(int i = 0; i < pElems.size(); i++){
			if(pElems.get(i) > qElems.get(i)){
				return false;
			}
		}
		return true;
	}
	/**
	 * To sort the population by order of their level of dominant.</br>
	 * One set contains individuals with the same level of dominant.
	 * @param pop population not sorted
	 * @return different sets of different levels of dominant
	 */
	public static ArrayList<ArrayList<Individual>> sort(ArrayList<Individual> pop){
		//initialize the non dominated set pn
		ArrayList<ArrayList<Individual>> pn = new ArrayList<ArrayList<Individual>>();
		for(int i = 0; i < pop.size(); i++){
			pn.add(new ArrayList<Individual>());
		}
		
		for(int i=0; i<pop.size(); i++){
			pop.get(i).numDominated = 0;
			pop.get(i).setDominant.clear();
		}
			
		//calculate the number of dominating and the dominating set
		for(int i = 0; i < pop.size(); i++){
			Individual pIndividual = pop.get(i);
			for(int j = 0; j < pop.size(); j++){
				if(j != i){//if the two individuals are different
					Individual qIndividual = pop.get(j);
					if(pDomQ(pIndividual, qIndividual)){
						pIndividual.addIndividualDominant(qIndividual);
					}
					else if(pDomQ(qIndividual, pIndividual)){
						pIndividual.changeNumDominated(1);
					}
				}
			}
			if(pIndividual.numDominated == 0)
				pn.get(0).add(pIndividual);
		}
		
		//calculate pn
		for(int i = 0; pn.get(i).size() != 0; ){
			ArrayList<Individual> h = new ArrayList<Individual>();
			for(Individual pIndividual : pn.get(i)){
				for(Individual qIndividual : pIndividual.setDominant){
					qIndividual.changeNumDominated(-1);
					if(qIndividual.numDominated == 0)
						h.add(qIndividual);
				}//end for q
			}// end for p
			i++;
			pn.set(i, h);  
		}
		
		return pn;
	} 

}
