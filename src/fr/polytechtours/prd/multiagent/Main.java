package fr.polytechtours.prd.multiagent;

import fr.polytechtours.prd.multiagent.util.Commun;

public class Main {

	public static void main(String[] args) {
		Commun.createRandomJobsAndResources(100, 3, 40, "instance-100-2-3-40.data");
	}

}
