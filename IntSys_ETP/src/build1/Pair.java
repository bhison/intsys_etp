package build1;

import java.util.LinkedList;

/**
 * A datatype class almost entirely lifted from Andy King's notes. Stores a route (list of consecutive
 * puzzle configurations as represented by strings) and a rank (either just the actual size of the
 * route or the route size + a heuristic factor)
 * 
 * @author Tim, Andy King
 * @version 08/04/2015
 *
 */
public class Pair implements Comparable<Pair>
{
	
	private double rank;
	private LinkedList<String> route;
	
	/**
	 * The constructor method simply stores the key data types
	 * @param rank How important this datapiece should be considered by a priority queue
	 * @param route The route taken between the puzzle start and the last in this list
	 */
	Pair(int rank, LinkedList<String> route)
	{	this.rank = rank;
		this.route = route; 
	}
	
	/**
	 * Returns the rank
	 * @return The rank
	 */
	public double getRank()
	{	return rank;
	}
	
	/**
	 * Returns the route
	 * @return The route
	 */
	public LinkedList<String> getRoute()
	{	return route;
	}
	
	/**
	 * Returns int representation of whether this is higher or lower rank than another pair
	 * @return 1 if this is higher rank than that being compared, -1 if lower, 0 if equal
	 */
	public int compareTo(Pair pair)
	{	if (rank > pair.getRank()) return 1;
		else if (rank < pair.getRank()) return -1;
		else return 0;
	}
	
}