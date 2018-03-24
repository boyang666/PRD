package fr.polytechtours.prd.multiagent.util;
/**
 * Class Timer if used to store the start time and the end time for executing the algorithm.<br>
 * One method for computing the time consumed is also provided.
 * 
 * @author Boyang Wang
 * @version 1.0
 * @since 24 Mars, 2018
 *
 */
public class Timer {
	/**
	 * start time of algorithm
	 */
	private long start;
	/**
	 * end time of algorithm
	 */
	private long end;
	/**
	 * calculate the time consumed
	 * @return time consumed
	 */
	public double calculateTimeConsume(){
		return ((double)end - (double)start) / 1000;
	}
	/**
	 * getter of start
	 * @return start time
	 */
	public long getStart() {
		return start;
	}
	/**
	 * setter of start
	 * @param start start time
	 */
	public void setStart(long start) {
		this.start = start;
	}
	/**
	 * getter of end time
	 * @return end time
	 */
	public long getEnd() {
		return end;
	}
	/**
	 * setter of end time
	 * @param end end time
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	
	
}
