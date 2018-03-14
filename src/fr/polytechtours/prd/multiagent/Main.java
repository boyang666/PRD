package fr.polytechtours.prd.multiagent;

import fr.polytechtours.prd.multiagent.util.Commun;

public class Main {

	public static void main(String[] args) {
		Commun.createRandomJobsAndResources(50, 3, 40, "instance-50-2-3-40.data");
	}

}
