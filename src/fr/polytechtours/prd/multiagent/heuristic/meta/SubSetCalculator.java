package fr.polytechtours.prd.multiagent.heuristic.meta;

import java.util.ArrayList;
import java.util.HashMap;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;
import fr.polytechtours.prd.multiagent.util.Commun;

public class SubSetCalculator {
	
	public static final int TYPE_START = 0;
	public static final int TYPE_END = 1;

	private class Event implements Comparable<Event>{
		int id = 0;
		int time = 0;
		int type = TYPE_START;
		
		@Override
		public int compareTo(Event o) {
			Event other = (Event)o;
			if(this.time < other.time){
				return -1;
			}else if(this.time > other.time){
				return 1;
			}else{
				if(this.type == TYPE_END && other.type == TYPE_START){
					return -1;
				}
				else if(this.type == TYPE_START && other.type == TYPE_END){
					return 1;
				}else{
					return this.id - other.id;
				}
			}
		}
	}
	
	public ArrayList<Event> init(){
		ArrayList<Event> events = new ArrayList<Event>();
		for(int i=0; i<Data.nbJobs; i++){
			Event e1 = new Event();
			Event e2 = new Event();
			e1.id = Data.jobs.get(i).id;
			e2.id = Data.jobs.get(i).id;
			e1.type = TYPE_START;
			e2.type = TYPE_END;
			e1.time = Data.jobs.get(i).start;
			e2.time = Data.jobs.get(i).end;
			events.add(e1);
			events.add(e2);
		}
		java.util.Collections.sort(events);;
		return events;
	}
	
	public ArrayList<ArrayList<Integer>> calculate(){
		ArrayList<Event> events = init();
		int inc = 0;
		ArrayList<Integer> S = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> subSet = new ArrayList<ArrayList<Integer>>();
		//int k = 0;
		
		for(int h=0; h<events.size(); h++){
			if(events.get(h).type == TYPE_START){
				S.add(events.get(h).id);
				inc = 0;
			}
			else{
				if(inc == 0){
					//k++;
					ArrayList<Integer> temp = new ArrayList<Integer>();
					temp.addAll(S);
					subSet.add(temp);
					inc = 1;
				}
				for(int iter=0 ;iter<S.size(); iter++){
					if(S.get(iter) == events.get(h).id){
						S.remove(iter);
						break;
					}
				}
			}
		}
		
		return subSet;
	}
	
	public static void main(String[] args){
		HashMap<String, Object> hashmap = Commun.ReadDataFromFile("instance-100-2-3-40.data", Job.TYPE_FACTOR_SORT_MAX);
		Data.jobs = (ArrayList<Job>) hashmap.get("jobs");
		Data.machine = (Machine)hashmap.get("machine");
		Data.nbJobs = (int) hashmap.get("numJob");
		Data.nbJobsA = (int) hashmap.get("numJobAgentA");
		Data.maxEnd = Commun.getMaxEnd(Data.jobs);
		
		SubSetCalculator calculator = new SubSetCalculator();
		ArrayList<ArrayList<Integer>> result = calculator.calculate();
		for(int i=0; i<result.size(); i++){
			StringBuilder builder = new StringBuilder("{");
			for(int j=0; j<result.get(i).size(); j++){
				builder.append(result.get(i).get(j)+" , ");
			}
			builder.append("}");
			System.out.println(builder.toString());
		}
	}
}
