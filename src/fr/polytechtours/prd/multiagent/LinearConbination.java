package fr.polytechtours.prd.multiagent;

import java.math.BigDecimal;
import java.util.ArrayList;
import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;


public class LinearConbination {
	
	public void initialWeight(ArrayList<Job> jobs, double valA){
		for(int i=0; i<jobs.size(); i++){
			if(jobs.get(i).belongTo.equals("A")){
				jobs.get(i).weight = valA;
			}
			else
				jobs.get(i).weight = 1-valA;
		}
	}

	public void execute(ArrayList<Job> jobs, Machine machine, double weight){
		/*HashMap<String, Object> data = Commun.ReadDataFromFile("instance-10-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		ArrayList<Job> jobs = (ArrayList<Job>) data.get("jobs");
		Machine machine = (Machine) data.get("machine");*/
		
		
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
