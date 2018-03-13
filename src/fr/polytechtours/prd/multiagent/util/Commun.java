package fr.polytechtours.prd.multiagent.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fr.polytechtours.prd.multiagent.model.Job;
import fr.polytechtours.prd.multiagent.model.Machine;

/**
 * 
 * This class propose the common methods for the general use
 * 
 * Specially, the method for creating jobs and saving to files, 
 * the method for reading jobs from files,
 * the method for getting the end time maximum of a sequence of jobs
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since Nov 30, 2017
 * 
 */
public class Commun {
	
	/**
	 * Method used to get the end time maximum of a sequence of jobs sorted
	 * 
	 * @param jobs a list of jobs
	 * @return end time max of a sequence of jobs
	 */
	public static int getMaxEnd(ArrayList<Job> jobs){
		int maxEnd = jobs.get(0).end;
		for(int i=0; i<jobs.size(); i++){
			if(jobs.get(i).end > maxEnd) {
				maxEnd = jobs.get(i).end;
			}
		}
		
		return maxEnd;
	}
	
	/**
	 * Method to create random jobs and to save a file
	 * The total amount of each resource is fixed to 1000
	 * Each consumption of resource is set randomly between 1 and 1000
	 * Each start time is set randomly between 1 and 1440 mns (one day)
	 * Each end time is set randomly between s+1 and 1440-s
	 * 
	 * @param nbJobs total number of jobs
	 * @param nbResouorces total number of resources
	 * @param percJobAgentA percentage of jobs which belongs to agent A
	 * @param filename file name to save jobs
	 */
	public static void createRandomJobsAndResources(int nbJobs, int nbResouorces, int percJobAgentA, String filename){
		ArrayList<Job> jobs = new ArrayList<Job>();
		Machine machine = new Machine();
		for(int i=0; i<nbResouorces; i++)
			machine.resources.add(1000);
		
		int nbJobsAgentA = nbJobs*percJobAgentA / 100;
		Random random = new Random();
		for(int i=0 ;i<nbJobs; i++){
			Job job = new Job();
			job.id = i;
			job.start = random.nextInt(1440);
			if(job.start == 1439)
				job.end = 1440;
			else
				job.end = job.start + 1 + random.nextInt((1440 - job.start - 1));
			for(int j=0; j<nbResouorces; j++){
				job.consumes.add(1 + random.nextInt(1000));
			}
			if(i < nbJobsAgentA){
				job.belongTo = "A";
			}
			else{
				job.belongTo = "B";
			}
			jobs.add(job);
		}
		
		File file = new File(filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(nbJobs+" "+machine.resources.size()+" "+nbJobsAgentA);
			bw.newLine();
			for(int i=0; i<machine.resources.size(); i++){
				bw.write(machine.resources.get(i)+" ");
			}
			bw.newLine();
			for(int i=0; i<jobs.size(); i++){
				bw.write(jobs.get(i).id + " " + jobs.get(i).start + " " + jobs.get(i).end + " " + jobs.get(i).belongTo + " ");
				for(int j=0; j<machine.resources.size(); j++){
					bw.write(jobs.get(i).consumes.get(j) + " ");
				}
				if(i != jobs.size() -1)
					bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read from a file to load all the jobs saved in this file
	 * 
	 * The result to return is of type the HashMap: 
	 * the keys are machine, jobs which are related to a machine and a list of jobs, 
	 * numJob for number of job, numResource for number of resources,
	 * numJobAgentA for number of job of agent A
	 * 
	 * @param fileName name of file to read
	 * @param typeOfSort the type of sorting
	 * @see fr.polytechtours.prd.multiagent.model.Job
	 * @return a HashMap with a machine (key:machine) and a list of jobs (key:jobs)
	 */
	public static HashMap<String, Object> ReadDataFromFile(String fileName, int typeOfSort){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			
			line=reader.readLine();
			String item[] = line.split(" ");
			int numJob = Integer.parseInt(item[0]);
			int numResource = Integer.parseInt(item[1]);
			int numJobAgentA = Integer.parseInt(item[2]);
			result.put("numJob", numJob);
			result.put("numResource", numResource);
			result.put("numJobAgentA", numJobAgentA);
			
			line=reader.readLine();
			item = line.split(" ");
			Machine machine = new Machine();
			for(int i=0; i<numResource; i++){
				machine.resources.add(Integer.parseInt(item[i]));
			}
			result.put("machine", machine);
			
			List<Job> jobs = new ArrayList<Job>();
            while((line=reader.readLine())!=null){ 
                item = line.split(" ");
                Job job = new Job();
                job.id = Integer.parseInt(item[0]);
                job.start = Integer.parseInt(item[1]);
                job.end = Integer.parseInt(item[2]);
                job.belongTo = item[3];
                for(int i=0; i<numResource; i++){
                	job.consumes.add(Integer.parseInt(item[i+4]));
                }
                job.weight = 1;
                job.calculateFactorOfSort(typeOfSort);
                jobs.add(job);
            }
            result.put("jobs", jobs);
            
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
