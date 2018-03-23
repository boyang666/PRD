package fr.polytechtours.prd.multiagent.exact;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.IAlgorithm;
import fr.polytechtours.prd.multiagent.model.Data;
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
public class LinearCombination implements IAlgorithm{
	/**
	 * data object with parameters
	 */
	public Data data;
	
	/**
	 * Function to initial the weight for the jobs of agent A and agent B<br>
	 * Jobs of agent A are weighted with <strong>coefficient</strong> while jobs of agent B are weighted with <strong>1-coefficient</strong>
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
	 * Function to execute the linear combination algorithm.<br>
	 * We use CPLEX to solve the problem
	 * 
	 * @return a hashmap with keys as :
	 * <ul>
	 * <li>"solved" : boolean, whether the problem has been solved</li>
	 * <li>"obj_value" : int, optimal value of the objective function</li>
	 * <li>"weight" : double, value of weight for jobs of agent A</li>
	 * <li>"result_x" : int[], sequence of result</li>
	 * </ul>
	 */
	@Override
	public HashMap<String, Object> execute(){
		HashMap<String, Object> results = new HashMap<String, Object>();
		boolean solved = false;
		int T = Commun.getMaxEnd(data.jobs);
		
		this.initialWeight(data.jobs, data.weight);
		
		try {
			IloCplex cplex = new IloCplex();
		
			IloIntVar[] x = cplex.boolVarArray(data.jobs.size());
			
			double[] objvals = new double[data.jobs.size()];
			for(int i=0; i<data.jobs.size(); i++){
				objvals[i] = data.jobs.get(i).weight;
			}
			cplex.addMinimize(cplex.scalProd(x, objvals));
			
			IloIntExpr[][] y = new IloIntExpr[data.jobs.size()][T];
			for(int i=0; i<data.jobs.size(); i++){
				for(int t=0; t<T; t++){
					y[i][t] = cplex.boolVar();
				}
			}
			
			
			for(int i=0; i<data.jobs.size(); i++){
				IloIntExpr Y = cplex.intExpr() ;
				for(int t=data.jobs.get(i).start; t<data.jobs.get(i).end; t++){
					
					Y = cplex.sum(y[i][t], Y);  
				}
				
				cplex.addEq(Y, cplex.prod((data.jobs.get(i).end - data.jobs.get(i).start),(cplex.diff(1, x[i]))));
			}
			
			
			for(int j=0; j<data.machine.resources.size(); j++){
				for(int t=0; t<T; t++){
					IloIntExpr Y2 = cplex.intExpr() ;
					for(int i=0; i<data.jobs.size(); i++){
						Y2 = cplex.sum(cplex.prod(data.jobs.get(i).consumes.get(j), y[i][t]), Y2);
					}
					
					cplex.addLe(Y2, data.machine.resources.get(j));
				}
			}
			
			cplex.setOut(null);
			
			if (cplex.solve()) {  
                /*cplex.output().println("Solution status = " + cplex.getStatus());  
                cplex.output().println("Solution value = " + new BigDecimal(cplex.getObjValue()).setScale(0, BigDecimal.ROUND_HALF_UP));  
                double[] val = cplex.getValues(x);  
                for (int j = 0; j < val.length; j++){
                	
                	cplex.output().println("x" + j + "  = " + new BigDecimal(val[j]).setScale(0, BigDecimal.ROUND_HALF_UP));  
                }*/
                
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
                results.put("weight", data.weight);
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

	@Override
	public void loadParam(Data data) {
		this.data = data;
	}


	@Override
	public void generateParetoFront() {
		for(data.weight = 0.05; data.weight <= 1; data.weight = data.weight + 0.05){
			this.loadParam(data);
			HashMap<String, Object> result = this.execute();
			boolean solved = (boolean) result.get("solved");
			if(solved){
				int[] result_x = (int[]) result.get("result_x");
				int obj_v_A = 0;
				int obl_v_B = 0;
				for(int i=0; i<result_x.length; i++){
					if(result_x[i] == 1 && i<data.nbJobsA){
						obj_v_A++;
					}
					else if(result_x[i] == 1 && i>=data.nbJobsA){
						obl_v_B++;
					}
				}
				System.out.println("Solution A : "+obj_v_A+" , "+"Solution B : "+obl_v_B);
			}
			
		}
		
	}

}
