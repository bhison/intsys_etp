package build1;

import java.util.LinkedList;

public class Pair implements Comparable<Pair>
{
	
	private double rank;
	private LinkedList<String> route;
	
	Pair(int rank, LinkedList<String> route)
	{	this.rank = rank;
		this.route = route; 
	}
	
	public double getRank()
	{	return rank;
	}
	
	public LinkedList<String> getRoute()
	{	return route;
	}
	
	public int compareTo(Pair pair)
	{	if (rank > pair.getRank()) return 1;
		else if (rank < pair.getRank()) return -1;
		else return 0; // return (int) (rank - pair.getRank());
	}
	
	public String toString() // force short debug traces
	{	return "(" + String.format("%.2f", rank) + ", " + route + ")";
	} 
	
}