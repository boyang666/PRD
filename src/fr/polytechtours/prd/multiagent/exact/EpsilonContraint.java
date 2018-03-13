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

public class EpsilonContraint {

	public HashMap<String, Object> execute(int epsilon, ArrayList<Job> jobs, Machine machine, int nbJobs, int nbJobsA, String agent){
		/*HashMap<String, Object> data = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		@SuppressWarnings("unchecked")
		ArrayList<Job> jobs = (ArrayList<Job>) data.get("jobs");
		Machine machine = (Machine) data.get("machine");
		int nbJobs = (int) data.get("numJob");
		int nbJobsA = (int) data.get("numJobAgentA");*/
		
		boolean solved = false;
		int T = Commun.getMaxEnd(jobs);
		HashMap<String, Object> results = new HashMap<String, Object>();
		
		
		try {
			IloCplex cplex = new IloCplex();
			
			//IloIntVar[] x_A = cplex.boolVarArray(nbJobsA);
			//IloIntVar[] x_B = cplex.boolVarArray(nbJobsB);
		
			IloIntVar[] x = cplex.boolVarArray(jobs.size());
			
			int[] objvals = new int[jobs.size()];
			for(int i=0; i<jobs.size(); i++){
				if(jobs.get(i).belongTo.equals(agent))
					objvals[i] = 0;
				else
					objvals[i] = 1;
			}
			cplex.addMinimize(cplex.scalProd(x, objvals));
			
			IloIntExpr[][] y = new IloIntExpr[jobs.size()][T];
			for(int i=0; i<jobs.size(); i++){
				for(int t=0; t<T; t++){
					y[i][t] = cplex.boolVar();
				}
			}
			
			int[] convals = new int[jobs.size()];
			for(int i=0; i<jobs.size(); i++){
				if(jobs.get(i).belongTo.equals(agent))
					convals[i] = 1;
				else
					convals[i] = 0;
			}
			cplex.addEq(cplex.scalProd(x, convals), epsilon);
			
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
			
			cplex.setOut(null);
			
			if (cplex.solve()) {  
                //cplex.output().println("Solution status = " + cplex.getStatus());  
                //cplex.output().println("Solution value = " + new BigDecimal(cplex.getObjValue()).setScale(0, BigDecimal.ROUND_HALF_UP));  
                int obj_f = new BigDecimal(cplex.getObjValue()).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                double[] val = cplex.getValues(x);  
                int[] result_x = new int[val.length];
                for (int j = 0; j < val.length; j++){
                	result_x[j] = new BigDecimal(val[j]).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                	//cplex.output().println("x" + j + "  = " + result_x[j]);  
                }
                
                
                solved = true;	
                
                results.put("solved", solved);
                results.put("obj_value", obj_f);
                results.put("epsilon", epsilon);
                results.put("result_x", result_x);
            }  
			else{
				results.put("solved", solved);
			}
			
            cplex.end();  
            
		} catch (IloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
}
