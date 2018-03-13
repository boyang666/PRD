package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;

public class NondominatedSetConstructor {

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
	
	public static ArrayList<ArrayList<Individual>> sort(ArrayList<Individual> pop){//len为种群规模
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
