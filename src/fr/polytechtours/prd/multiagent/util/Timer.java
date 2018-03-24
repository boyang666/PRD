package fr.polytechtours.prd.multiagent.util;

public class Timer {

	private long start;
	
	private long end;
	
	public double calculateTimeConsume(){
		return ((double)end - (double)start) / 1000;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
	
	
}
