package fr.polytechtours.prd.multiagent.exact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;

/**
 * Implementation of algorithm Epsilon Constraint </br>
 * This algorithm is an exact method to solve the multi-objectifs problems</br>
 * To solve the problem, we use the library of CPLEX as the solver.</br>
 * In order to get the optimal pareto front, we need to solve the symmetric problem  
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Jan 1, 2018
 *
 */
public class EpsilonConstraint {

	/**
	 * Function to execute the algorithm Epsilon Constraint</br>
	 * We use CPLEX to solve the problem
	 * 
	 * @param epsilon epsilon to import
	 * @param jobs list of jobs
	 * @param machine available machine with resources
	 * @param nbJobs number of jobs
	 * @param nbJobsA number of jobs of agent A
	 * @param agent which agent to consider as the constraint
	 * @return a hashmap with keys as :
	 * <ul>
	 * <li>"solved" : boolean, whether the problem has been solved</li>
	 * <li>"obj_value" : int, optimal value of the objectif function</li>
	 * <li>"epsilon" : int, value of epsilon</li>
	 * <li>"result_x" : int[], sequence of result</li>
	 * </ul>
	 */
	public HashMap<String, Object> execute(int epsilon, ArrayList<Job> jobs, Machine machine, int nbJobs, int nbJobsA, String agent){
		
		//initiation of variables
		boolean solved = false;
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		//get the max end time of all jobs
		int T = Commun.getMaxEnd(jobs);
		
		
		try {
			//initiation of cplex object
			IloCplex cplex = new IloCplex();
		
			//initiation of x as 0 for scheduled and 1 for not scheduled
			IloIntVar[] x = cplex.boolVarArray(jobs.size());
			
			//initiation of coefficient as 0 for jobs of agent on parameter and 1 for other jobs
			int[] objvals = new int[jobs.size()];
			for(int i=0; i<jobs.size(); i++){
				if(jobs.get(i).belongTo.equals(agent))
					objvals[i] = 0;
				else
					objvals[i] = 1;
			}
			
			//add object function
			cplex.addMinimize(cplex.scalProd(x, objvals));
			
			//initiation of temporary variables
			IloIntExpr[][] y = new IloIntExpr[jobs.size()][T];
			for(int i=0; i<jobs.size(); i++){
				for(int t=0; t<T; t++){
					y[i][t] = cplex.boolVar();
				}
			}
			
			//add constraint to cplex
			int[] convals = new int[jobs.size()];
			for(int i=0; i<jobs.size(); i++){
				if(jobs.get(i).belongTo.equals(agent))
					convals[i] = 1;
				else
					convals[i] = 0;
			}
			cplex.addEq(cplex.scalProd(x, convals), epsilon);
			
			//add constraint to cplex
			for(int i=0; i<jobs.size(); i++){
				IloIntExpr Y = cplex.intExpr() ;
				for(int t=jobs.get(i).start; t<jobs.get(i).end; t++){
					
					Y = cplex.sum(y[i][t], Y);  
				}
				
				cplex.addEq(Y, cplex.prod((jobs.get(i).end - jobs.get(i).start),(cplex.diff(1, x[i]))));
			}
			
			//add constraint to cplex
			for(int j=0; j<machine.resources.size(); j++){
				for(int t=0; t<T; t++){
					IloIntExpr Y2 = cplex.intExpr() ;
					for(int i=0; i<jobs.size(); i++){
						Y2 = cplex.sum(cplex.prod(jobs.get(i).consumes.get(j), y[i][t]), Y2);
					}
					
					cplex.addLe(Y2, machine.resources.get(j));
				}
			}
			
			//close cplex's output
			cplex.setOut(null);
			
			//call cplex to solve the problem
			if (cplex.solve()) {  // if the problem con be solved
				
				// optimal value of function
                int obj_f = new BigDecimal(cplex.getObjValue()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                
                // get the sequence of solution
                double[] val = cplex.getValues(x);  
                int[] result_x = new int[val.length];
                for (int j = 0; j < val.length; j++){
                	result_x[j] = new BigDecimal(val[j]).setScale(0, BigDecimal.ROUND_HALF_UP).intValue(); 
                }
                
                //set solved to true
                solved = true;	
                
                //add all elements to hashmap
                results.put("solved", solved);
                results.put("obj_value", obj_f);
                results.put("epsilon", epsilon);
                results.put("result_x", result_x);
            }  
			else{// if the problem cannot be solved
				results.put("solved", solved);
			}
			
            cplex.end();  
            
		} catch (IloException e) {
			e.printStackTrace();
		}
		return results;
	}
}
