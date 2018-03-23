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
public class EpsilonConstraint implements IAlgorithm{
	/**
	 * data object with parameters
	 */
	public Data data;

	/**
	 * Function to execute the algorithm Epsilon Constraint</br>
	 * We use CPLEX to solve the problem
	 * 
	 * @return a hashmap with keys as :
	 * <ul>
	 * <li>"solved" : boolean, whether the problem has been solved</li>
	 * <li>"obj_value" : int, optimal value of the objectif function</li>
	 * <li>"epsilon" : int, value of epsilon</li>
	 * <li>"result_x" : int[], sequence of result</li>
	 * </ul>
	 */
	public HashMap<String, Object> execute(){
		
		//initiation of variables
		boolean solved = false;
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		//get the max end time of all jobs
		int T = Commun.getMaxEnd(data.jobs);
		
		
		try {
			//initiation of cplex object
			IloCplex cplex = new IloCplex();
		
			//initiation of x as 0 for scheduled and 1 for not scheduled
			IloIntVar[] x = cplex.boolVarArray(data.jobs.size());
			
			//initiation of coefficient as 0 for jobs of agent on parameter and 1 for other jobs
			int[] objvals = new int[data.jobs.size()];
			for(int i=0; i<data.jobs.size(); i++){
				if(data.jobs.get(i).belongTo.equals(data.agent))
					objvals[i] = 0;
				else
					objvals[i] = 1;
			}
			
			//add object function
			cplex.addMinimize(cplex.scalProd(x, objvals));
			
			//initiation of temporary variables
			IloIntExpr[][] y = new IloIntExpr[data.jobs.size()][T];
			for(int i=0; i<data.jobs.size(); i++){
				for(int t=0; t<T; t++){
					y[i][t] = cplex.boolVar();
				}
			}
			
			//add constraint to cplex
			int[] convals = new int[data.jobs.size()];
			for(int i=0; i<data.jobs.size(); i++){
				if(data.jobs.get(i).belongTo.equals(data.agent))
					convals[i] = 1;
				else
					convals[i] = 0;
			}
			cplex.addEq(cplex.scalProd(x, convals), data.epsilon);
			
			//add constraint to cplex
			for(int i=0; i<data.jobs.size(); i++){
				IloIntExpr Y = cplex.intExpr() ;
				for(int t=data.jobs.get(i).start; t<data.jobs.get(i).end; t++){
					
					Y = cplex.sum(y[i][t], Y);  
				}
				
				cplex.addEq(Y, cplex.prod((data.jobs.get(i).end - data.jobs.get(i).start),(cplex.diff(1, x[i]))));
			}
			
			//add constraint to cplex
			for(int j=0; j<data.machine.resources.size(); j++){
				for(int t=0; t<T; t++){
					IloIntExpr Y2 = cplex.intExpr() ;
					for(int i=0; i<data.jobs.size(); i++){
						Y2 = cplex.sum(cplex.prod(data.jobs.get(i).consumes.get(j), y[i][t]), Y2);
					}
					
					cplex.addLe(Y2, data.machine.resources.get(j));
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
                results.put("epsilon", data.epsilon);
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
		Data data = this.data;
		for(data.epsilon = data.nbJobsA/2; data.epsilon<=data.nbJobsA; data.epsilon++){
			this.loadParam(data);
			HashMap<String, Object> result = this.execute();
			boolean solved = (boolean)result.get("solved");
			
			if(solved){//solve the symmetric problem
				
				int obj_v_B = (int)result.get("obj_value");
				
				Data dataSym = new Data();
				
				dataSym.jobs = data.jobs;
				dataSym.machine = data.machine;
				dataSym.nbJobs = data.nbJobs;
				dataSym.epsilon = obj_v_B;
				dataSym.nbJobsA = data.nbJobs - data.nbJobsA;
				dataSym.agent = "B";
				
				this.loadParam(dataSym);
				HashMap<String, Object> result_sym = this.execute();
				int obj_v_A = (int)result_sym.get("obj_value");
				
				int[] solution = (int[]) result_sym.get("result_x");
				StringBuilder str = new StringBuilder("[");
				for(int i=0; i<solution.length; i++){
					str.append(solution[i]).append(",");
				}
				str.append("]");
				System.out.println(str.toString());
				
				System.out.println("Solution A = "+obj_v_A+" , Solution B = "+obj_v_B);
			}	
		}
		
	}
}
