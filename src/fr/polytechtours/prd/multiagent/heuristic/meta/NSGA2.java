package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fr.polytechtours.prd.multiagent.heuristic.Greedy;
import fr.polytechtours.prd.multiagent.model.Job;

/**
 * This class provides the main loop and several global methods for NSGA2.</br> 
 * @author Boyang Wang
 * @version 1.0
 * @since Mars 10, 2018
 *
 */
public class NSGA2 {
	
	public ArrayList<Integer> initByGreedy(int epsilon){
		Greedy greedy = new Greedy();
		ArrayList<Job> sortedJobs = greedy.sortEpsilon(Data.jobs);
		ArrayList<Job> solution = greedy.executeEpsilon(sortedJobs, Data.nbJobsA, Data.machine, epsilon);
		ArrayList<Integer> gene = new ArrayList<Integer>();
		for(int i=0; i<Data.nbJobs; i++){
			boolean isIn = false;
			for(int j=0; j<solution.size(); j++){
				if(Data.jobs.get(i).id == solution.get(j).id){
					gene.add(0);
					isIn = true;
					break;
				}
			}
			if(!isIn){
				gene.add(1);
			}
		}
		
		StringBuilder str = new StringBuilder("");
		for(int i=0; i<gene.size(); i++){
			str.append(gene.get(i)+",");
		}
		System.out.println("gene: " + str.toString());
		return gene;
	}

	
	public ArrayList<Individual> initGroup(){
		ArrayList<Individual> group = new ArrayList<Individual>();
		Random random = new Random();
		int size = 0;
		//int epsilon = Data.nbJobsA/2;
		while(size < Constant.SIZE_POPULATION){
			Individual ind = new Individual();
			for(int j=0; j<Data.nbJobs; j++){
				//ind.genes.add(random.nextInt(2));
				
				/*if(j == size)ind.genes.add(0);
				else ind.genes.add(1);*/
				
				ind.genes.add(1);
			}
			
			if(size > 0){
				int index = random.nextInt(Data.nbJobsA);
				ind.genes.set(index, 0);
				index = Data.nbJobsA + random.nextInt(Data.nbJobs - Data.nbJobsA);
				ind.genes.set(index, 0);
			}
			
			
			/*ind.genes = this.initByGreedy(epsilon);
			epsilon++;*/
			
			ind.calculateValueObj();
			ind.validate();
			//System.out.println(ind.valide);
			
			boolean exist = true;
			if(ind.valide){
				if(group.size() == 0){
					group.add(ind);
					size++;
				}else{
					for(int k=0; k<group.size(); k++){
						exist = true;
						for(int iter=0; iter<Data.nbJobs; iter++){
							if(ind.genes.get(iter) != group.get(k).genes.get(iter)){
								exist = false;
								break;
							}
						}
						if(exist)break;
					}
					
					if(!exist){
						size++;
						group.add(ind);
					}
				}
			}
			System.out.println(exist);
			size++;
			group.add(ind);
		}
		
		//group = this.removeDuplicateFromArray(group);
		
		
		if(group.size() < Constant.SIZE_POPULATION){
			int sizeGroup = group.size();
			for(int i=0; i < Constant.SIZE_POPULATION - sizeGroup; i++){
				Individual ind = new Individual();
				ind.genes.addAll(group.get(i).genes);
				for(int j=Data.nbJobs - 1; j>=0; j--){
					if(group.get(i).genes.get(j) == 0){
						ind.genes.set(j, 1);
						break;
					}
				}
				ind.calculateValueObj();
				group.add(ind);
			}
		}
		/*for(Individual ind: group)
			System.out.println("A: "+ind.valuesObj.get(0)+" , B: "+ind.valuesObj.get(1));*/
		return group;
	}
	
	/**
	 * selection operation to select one individual to attend the crossover
	 * @param pop population
	 * @return index of individual chosen
	 */
	public int selection(ArrayList<Individual> pop){
		Random random = new Random();
		int p = random.nextInt(pop.size());
		int q = random.nextInt(pop.size());
		if(NondominatedSetConstructor.pDomQ(pop.get(p), pop.get(q))){
			return p;
		}
		else if(NondominatedSetConstructor.pDomQ(pop.get(q), pop.get(p))){
			return q;
		}
		else{
			if(random.nextBoolean()){
				return p;
			}
			else{
				return q;
			}
		}
	}
	
	/**
	 * crossover operation to create new children
	 * @param dad dad individual
	 * @param mum mom individual
	 * @return two children created by crossover
	 */
	public Individual[] crossOver(Individual dad, Individual mum){
		Random random = new Random();
		Individual[] children = new Individual[2];
		children[0] = new Individual();
		children[1] = new Individual();
		int numCross = 1+random.nextInt(Data.nbJobs-1);
		//int start = random.nextInt(Data.nbJobs - numCross);
		//int start = 0;
		//int end = start + numCross;
		//int end = Data.nbJobs - 1;
		ArrayList<Integer> index = new ArrayList<Integer>();
		while(index.size() < numCross){
			Integer pos = random.nextInt(Data.nbJobs);
			if(!index.contains(pos)){
				index.add(pos);
			}
		}
		Collections.sort(index);
		
		if(random.nextDouble() < Constant.PROB_CROSSOVER){
			/*for(int i=0; i<dad.genes.size(); i++){
				if(i < start || i >= end){
					children[0].genes.add(dad.genes.get(i));
					children[1].genes.add(mum.genes.get(i));
				}
				else {
					children[0].genes.add(mum.genes.get(i));
					children[1].genes.add(dad.genes.get(i));
				}
			}*/
			for(int i=0; i<dad.genes.size(); i++){
				children[0].genes.add(dad.genes.get(i));
				children[1].genes.add(mum.genes.get(i));
			}
			for(int i=0; i<index.size(); i++){
				children[0].genes.set(index.get(i), mum.genes.get(index.get(i)));
				children[1].genes.set(index.get(i), dad.genes.get(index.get(i)));
			}
		}
		else{
			for(int i=0; i<dad.genes.size(); i++){
				children[0].genes.add(dad.genes.get(i));
				children[1].genes.add(mum.genes.get(i));
			}
		}
		
		
		
		//to calculate their values of objective functions
		children[0].calculateValueObj();
		children[1].calculateValueObj();
		
		return children;
	}
	
	/**
	 * Mutation operation
	 * @param children children created after crossover
	 * @return children after mutation
	 */
	public Individual[] mutation(Individual[] children){
		Random random = new Random();
		
		for(int index=0; index<2; index++){
			int pos = random.nextInt(Data.nbJobs);
			if(random.nextDouble() < Constant.PROB_MUTATION){
				if(children[index].genes.get(pos) == 1){
					children[index].genes.set(pos, 0);
				}else{
					children[index].genes.set(pos, 1);
				}
			}
			
			children[index].calculateValueObj();
		}
		
		// to verify the children's feasibility
		children[0].validate();
		children[1].validate();
		return children;
	}
	
	/**
	 * remove the duplicate individuals from the population
	 * @param pop population
	 * @return population without duplicate
	 */
	public ArrayList<Individual> removeDuplicateFromArray(ArrayList<Individual> pop){
		for (int i = 0; i < pop.size() - 1; i++) {
			for (int j = pop.size() - 1; j > i; j--) {
				/*int k=0;
				for(k=0; k<pop.get(j).genes.size(); k++){
					if(pop.get(j).genes.get(k) != pop.get(i).genes.get(k)){
						break;
					}
				}
				if(k == pop.get(j).genes.size()){
					pop.remove(j);
				}*/
				if (pop.get(j).valuesObj.get(0) == pop.get(i).valuesObj.get(0) && pop.get(j).valuesObj.get(1) == pop.get(i).valuesObj.get(1)) {
					pop.remove(j);
				}
			}
		}
		return pop;
	}
	
	/**
	 * print the result to console
	 * @param pop population
	 * @param generation number of iteration
	 */
	public void outputInd(ArrayList<Individual> pop, String generation){
		for(Individual ind : pop){
			StringBuilder str = new StringBuilder("");
			str.append("Value A: ").append(ind.valuesObj.get(0)).append(" , ").append("Value B: ").append(ind.valuesObj.get(1));
			str.append(" ---- ");
			str.append(generation);
			System.out.println(str.toString());
			
		}
		System.out.println(generation + " finish");
	}
	
	/**
	 * Main loop for NSGA2</br>
	 * The algorithm consists of these steps:
	 * <ol>
	 * <li>Initiation of population</li>
	 * <li>Crossover, mutation</li>
	 * <li>Construct non dominated sets</li>
	 * <li>Calculate crowded distances</li>
	 * <li>Reconstruct the population£¬ if iteration not end, go to 2</li>
	 * </ol>
	 * @return better solution found by NSGA2
	 */
	public ArrayList<Individual> execute() {
		Random random = new Random();
		
		ArrayList<ArrayList<Individual>> nds = null; // save the non dominated set
		
		ArrayList<Individual> group = initGroup(); // initialize the population
		
		
		System.out.println("begin");
		for (int ng = 1; ng <= Constant.NUM_ITERATION; ng++) // the start of algorithm
		{
			ArrayList<Individual> tempGroup = new ArrayList<Individual>(); // temporary population
			
			for (int i = 0; i < group.size(); i++) {
				tempGroup.add(group.get(i));
			}
			
			// System.out.println("\nNO. " + ng + ":");
			for (int i = 0; i < group.size(); i++) {// to the genetic operations
				int dad, mum;
				Individual dadMoi = null;
				Individual mumMoi = null;
				
				do {
					dad = selection(group);// selection using competition strategy
					mum = selection(group);
				} while (dad == mum); // when the dad and mom are the same
				
				dadMoi = group.get(dad);
				mumMoi = group.get(mum);
				
				Individual[] temp = crossOver(dadMoi, mumMoi);// crossover 
				temp = mutation(temp);// mutation
				
				for (int j = 0; j < 2; j++) {
					if (temp[j].valide && NondominatedSetConstructor.pDomQ(temp[j], group.get(dad))) {
						group.set(dad, temp[j]);
						break;
					}
					if (temp[j].valide && NondominatedSetConstructor.pDomQ(temp[j], group.get(mum))) {
						group.set(mum, temp[j]);
					}
				}
				
			} // end of one iteration

			group.addAll(tempGroup);// combine the two populations
			
			
			nds = NondominatedSetConstructor.sort(group);// sort all individuals
			
			
			
			ArrayList<Individual> newGroup = new ArrayList<Individual>();
			
			int iteration = 0;
			while (iteration < Constant.SIZE_POPULATION && (newGroup.size() + removeDuplicateFromArray(nds.get(iteration)).size() <= Constant.SIZE_POPULATION) && (nds.get(iteration).size() != 0)) {
				// when the new population are not all filled
			
				nds.set(iteration, removeDuplicateFromArray(nds.get(iteration)));
				newGroup.addAll(nds.get(iteration));
				iteration++;
			}
			
			int sizeTemp = newGroup.size();
			if(nds.get(iteration).size() == 0 && sizeTemp < Constant.SIZE_POPULATION){
				int i=0, j=0;
				while(newGroup.size() < Constant.SIZE_POPULATION){
					j = random.nextInt(nds.get(i).size());
					Individual ind = new Individual();
					ind.genes.addAll(nds.get(i).get(j).genes);
					while(true){
						int k = random.nextInt(ind.genes.size());
						if(ind.genes.get(k) == 0){
							ind.genes.set(k, 1);
							break;
						}
					}
					
					ind.calculateValueObj();
					newGroup.add(ind);
				}
			}else{
				CrowdingDistanceAssignment.distanceCalculator(nds.get(iteration));
				
				nds.set(iteration, CrowdingDistanceAssignment.sortByDistance(nds.get(iteration)));
				int size = newGroup.size();
				for (int j = 0; j < Constant.SIZE_POPULATION - size; j++) {
					newGroup.add(nds.get(iteration).get(j));
				}
			}
			
			
			group = newGroup;
			outputInd(nds.get(0), "G" + ng);
		} // all over;

		return nds.get(0);
	}
	
	
}
