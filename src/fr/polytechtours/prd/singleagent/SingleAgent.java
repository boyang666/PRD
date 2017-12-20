package fr.polytechtours.prd.singleagent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import ilog.concert.IloException;
import ilog.concert.IloIntExpr;
import ilog.concert.IloIntVar;
import ilog.cplex.IloCplex;

public class SingleAgent {

	public static void main(String[] args) {
		
		HashMap<String, Object> data = new HashMap<String, Object>();
		data = Commun.ReadDataFromFile("instance-20-1-3.data");
		ArrayList<Job> jobs = (ArrayList<Job>) data.get("jobs");
		Machine machine = (Machine) data.get("machine");
		for(int i=0; i<jobs.size(); i++){
			//System.out.println(jobs.get(i).id);
		}
		for(int i=0; i<machine.resources.size(); i++){
			//System.out.println(machine.resources.get(i));
		}
		
		Greedy greedy = new Greedy();
		int T = greedy.getMaxEnd(jobs);
		
		try {
			IloCplex cplex = new IloCplex();
			IloIntVar[] x = cplex.boolVarArray(jobs.size());
			
			cplex.addMinimize(cplex.sum(x));
			
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
