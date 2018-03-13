package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;

public class Individual implements Calculate{

	public ArrayList<Integer> genes;
	
	public ArrayList<Integer> valuesObj;
	
	public int numDominated;
	
	public ArrayList<Individual> setDominant;
	
	public double crowdedDistance = 0.0;
	
	public boolean valide = true;
	
	public Individual(){
		genes = new ArrayList<Integer>();
		valuesObj = new ArrayList<Integer>();
		numDominated = 0;
		setDominant = new ArrayList<Individual>();
	}
	
	public void changeNumDominated(int change){
		this.numDominated += change;
	}
	
	public void addIndividualDominant(Individual ind){
		this.setDominant.add(ind);
	}

	@Override
	public void calculateValueObj() {
		valuesObj.clear();
		valuesObj.add(0);
		valuesObj.add(0);
		
		for(int i=0; i<Data.nbJobs; i++){
			if(Data.jobs.get(i).belongTo.equals("A")){
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
		for(int t=1; t<=Data.maxEnd; t++){
			consumes.clear();
			for(int k=0; k<Data.machine.resources.size(); k++){
				consumes.add(0);
			}
			for(int i=0; i<Data.jobs.size(); i++){
				if(Data.jobs.get(i).start <= t && Data.jobs.get(i).end >= t && genes.get(i) == 0){
					for(int j=0; j<consumes.size(); j++){
						consumes.set(j, consumes.get(j) + Data.jobs.get(i).consumes.get(j));
					}
				}
			}
			for(int iter=0; iter<consumes.size(); iter++){
				if(consumes.get(iter) > Data.machine.resources.get(iter)){
					this.valide = false;
					break;
				}
			}
			if(!this.valide)break;
		}
	}
	
}
