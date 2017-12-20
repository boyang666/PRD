package fr.polytechtours.prd.singleagent;

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

public class Commun {
	
	public static void createRandomJobsAndResources(int nbJobs, int nbResouorces, String filename){
		ArrayList<Job> jobs = new ArrayList<Job>();
		Machine machine = new Machine();
		for(int i=0; i<nbResouorces; i++)
			machine.resources.add(1000);
		
		Random random = new Random();
		for(int i=0 ;i<nbJobs; i++){
			Job job = new Job();
			job.id = i;
			job.start = random.nextInt(24);
			if(job.start == 23)
				job.end = 24;
			else
				job.end = job.start + 1 + random.nextInt((24 - job.start - 1));
			for(int j=0; j<nbResouorces; j++){
				job.consumes.add(1 + random.nextInt(1000));
			}
			jobs.add(job);
		}
		
		File file = new File(filename);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(nbJobs+" "+machine.resources.size());
			bw.newLine();
			for(int i=0; i<machine.resources.size(); i++){
				bw.write(machine.resources.get(i)+" ");
			}
			bw.newLine();
			for(int i=0; i<jobs.size(); i++){
				bw.write(jobs.get(i).id + " " + jobs.get(i).start + " " + jobs.get(i).end + " ");
				for(int j=0; j<machine.resources.size(); j++){
					bw.write(jobs.get(i).consumes.get(j) + " ");
				}
				if(i != jobs.size() -1)
					bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static HashMap<String, Object> ReadDataFromFile(String fileName){
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line = null;
			
			line=reader.readLine();
			String item[] = line.split(" ");
			int numJob = Integer.parseInt(item[0]);
			int numResource = Integer.parseInt(item[1]);
			result.put("numJob", numJob);
			result.put("numResource", numResource);
			
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
                for(int i=0; i<numResource; i++){
                	job.consumes.add(Integer.parseInt(item[i+3]));
                }
                job.calculateFactorOfSort();
                jobs.add(job);
            }
            result.put("jobs", jobs);
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}