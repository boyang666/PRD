package fr.polytechtours.prd.multiagent.exact;

import java.math.BigDecimal;
import java.util.ArrayList;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;

/**
 * Implementation of algorithm Linear Combination </br>
 * This algorithm is an exact method to solve the multi-objectifs problems</br>
 * To solve the problem, we use the library of CPLEX as the solver.</br>
 * We need to change the coefficient to get the pareto front.</br>
 * This method is not as good as {@link EpsilonConstraint}. 
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Feb 1, 2018
 *
 */
public class LinearCombination {
	
	/**
	 * Function to initial the weight for the jobs of agent A and agent B
	 * 
	 * @param jobs list of jobs
	 * @param valA coefficient of jobs for agent A
	 */
	public void initialWeight(ArrayList<Job> jobs, double valA){
		for(int i=0; i<jobs.size(); i++){
			if(jobs.get(i).belongTo.equals("A")){// for jobs of agent A
				jobs.get(i).weight = valA;
			}
			else // for jobs of agent B
				jobs.get(i).weight = 1-valA;
		}
	}

	/**
	 * Function to execute the linear combination algorithm
	 * 
	 * @param jobs list of jobs
	 * @param machine machine with resources
	 * @param weight weight for jobs of agent A
	 */
	public void execute(ArrayList<Job> jobs, Machine machine, double weight){
		
		int T = Commun.getMaxEnd(jobs);
		
		this.initialWeight(jobs, weight);
		
		try {
			IloCplex cplex = new IloCplex();
		
			IloIntVar[] x = cplex.boolVarArray(jobs.size());
			
			double[] objvals = new double[jobs.size()];
			for(int i=0; i<jobs.size(); i++){
				objvals[i] = jobs.get(i).weight;
				System.out.println(objvals[i]);
			}
			cplex.addMinimize(cplex.scalProd(x, objvals));
			
			IloIntExpr[][] y = new IloIntExpr[jobs.size()][T];
			for(int i=0; i<jobs.size(); i++){
				for(int t=0; t<T; t++){
					y[i][t] = cplex.boolVar();
				}
			}
			
			
			for(int i=0; i<jobs.size(); i++){
				IloIntExpr Y = cplex.intExpr() ;
				for(int t=jobs.get(i).start; t<jobs.get(i).end; t++){
					
					Y = cplex.sum(y[i][t], Y);  
				}
				
				cplex.addEq(Y, cplex.prod((jobs.get(i).end - jobs.get(i).start),(cplex.diff(1, x[i]))));
			}
			
			
			for(int j=0; j<machine.resources.size(); j++){
				for(int t=0; t<T; t++){
					IloIntExpr Y2 = cplex.intExpr() ;
					for(int i=0; i<jobs.size(); i++){
						Y2 = cplex.sum(cplex.prod(jobs.get(i).consumes.get(j), y[i][t]), Y2);
					}
					
					cplex.addLe(Y2, machine.resources.get(j));
				}
			}
			
			if (cplex.solve()) {  
                cplex.output().println("Solution status = " + cplex.getStatus());  
                cplex.output().println("Solution value = " + new BigDecimal(cplex.getObjValue()).setScale(0, BigDecimal.ROUND_HALF_UP));  
                double[] val = cplex.getValues(x);  
                for (int j = 0; j < val.length; j++){
                	
                	cplex.output().println("x" + j + "  = " + new BigDecimal(val[j]).setScale(0, BigDecimal.ROUND_HALF_UP));  
                }
                	
            }  
            cplex.end();  
            
		} catch (IloException e) {
			e.printStackTrace();
		}
		
	}

}
