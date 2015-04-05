package build1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Main 
{
	
	private int gameWidth;
	private int gameHeight;
	
	int depth = 0;
	
	private static final boolean OUTPUT_TO_CONSOLE = false; //Set this to false to output as files
	
	public Main(String[] args)
	{	String puzzleString = args[0];
		int gameWidth = 3; //Change this to alter default
		if(args.length > 1) gameWidth = Integer.parseInt(args[1]);
		this.gameWidth = gameWidth;
//		System.out.println("gameWidth set to: " + gameWidth);
		int puzzleSize = (puzzleString.length() -5 ) /2;
//		System.out.println("puzzleSize set to: " + puzzleSize);
		gameHeight =  puzzleSize / gameWidth;
//		System.out.println("gameHeight set to: " + gameHeight);
		String start = puzzleString.substring(0, puzzleSize);
		String target = puzzleString.substring(puzzleSize + 1, puzzleSize * 2 + 1);
		System.out.println("Puzzle string is: " + puzzleString);
		LinkedList<String> solutionPath = uniformCost(start,target);
		if(solutionPath == null) System.out.println("ERROR: Something went wrong! No solution found!");
		else 
		{	try	{ createSolutionFile(puzzleString, solutionPath, OUTPUT_TO_CONSOLE); }
			catch(IOException e) { System.out.println(e); }
		}
		System.out.println("Done!");
	}
	
	/**
	 * TODO: Get this to operate correctly. Create "actualDistance" method then link it in as
	 * main solution method.
	 * @param start
	 * @param dest
	 * @return
	 */
	private LinkedList<String> uniformCost(String start, String target)
	{	LinkedList<String> allCalculated = new LinkedList<String>();
		LinkedList<String> route = new LinkedList<String>(); 
		route.add(start);
		PriorityQueue pairs = new PriorityQueue();
		pairs.add(new Pair(0.0, route)); // uniform-cost
//		pairs.add(new Pair(estimateDistance(start, target), route)); // best 
		while (true)
		{	//System.out.println(pairs); 		// debug traces
			if (pairs.size() == 0) return null;		// no solutions exist
			Pair pair = (Pair) pairs.poll(); 		// retrieve and remove (log)
			route = pair.getRoute();
			String last = route.getLast();
			if (last.equals(target)) return route;
			LinkedList<String> nextConfigs = getAdjacencies(last);//graph.get(last);
			for (String next : nextConfigs)
			{	if (!allCalculated.contains(next))	//deja vu
				{	allCalculated.add(next);
					LinkedList<String> nextRoute = new LinkedList<String>(route); 
					nextRoute.addLast(next);	
					int distance = nextRoute.size(); // uniform 
					if(nextRoute.size() > depth)
					{	depth = nextRoute.size();
						System.out.println("New Depth = " + depth);
					}
	//				double distance = estimateDistance(next, target); // best-first 
					pairs.add(new Pair(distance, nextRoute)); // log too
				}
			}
		}
	}  
     
    private LinkedList<String> getAdjacencies(String stringConfig)
    {	int gapIndex = stringConfig.indexOf('_');
    	LinkedList<String> moveList = getValidMoveList(gapIndex);
    	LinkedList<String> adjacencyList = new LinkedList<String>();
    	for(String direction : moveList)
    	{	int tileIndex = gapIndex;
    		if(direction == "up") tileIndex += gameWidth;
    		else if(direction == "down") tileIndex -= gameWidth;
    		else if(direction == "left") tileIndex += 1;
    		else if(direction == "right") tileIndex -= 1;
    		
    		String tile = stringConfig.substring(tileIndex, tileIndex + 1);
    		StringBuilder newConfig = new StringBuilder();
    		for(int i = 0; i < stringConfig.length(); i++)
    		{	if(i == gapIndex) newConfig.append(tile);
    			else if(i == tileIndex) newConfig.append("_");
    			else newConfig.append(stringConfig.charAt(i));
    		}
    		adjacencyList.add(new String(newConfig));
    	}
    	return adjacencyList;
    }
	
	/**
	 * Takes a gap index and returns list of valid slides based on game dimensions
	 * @param gapIndex
	 * @return List of valid slide directions
	 */
	private LinkedList<String> getValidMoveList(int gapIndex)
	{	LinkedList<String> returnArray = new LinkedList<String>();
		if(gapIndex < gameWidth); else returnArray.add("down");
		if(gapIndex >= gameWidth * gameHeight - gameWidth); else returnArray.add("up");
		int column = gapIndex % gameWidth;
		if(column == 0); else returnArray.add("right");
		if(column == gameWidth - 1); else returnArray.add("left");		
		return returnArray;
	}

	private void createSolutionFile(String puzzleString, LinkedList<String> stringSolutionPath, 
			boolean consoleMode) throws IOException
	{	//TODO: Turn solution path in to LinkedList<char[]> or rewrite altogether
		LinkedList<char[]> solutionPath = new LinkedList<char[]>();
		for(String s : stringSolutionPath)
		{	solutionPath.add(s.toCharArray());			
		}
		PrintWriter writer = null;
		if(!consoleMode) writer = new PrintWriter(puzzleString, "UTF-8");
		for(int i = 0; i < gameHeight; i++)
		{	StringBuilder line = new StringBuilder();
			for(char[] config : solutionPath)
			{	StringBuilder configLine = new StringBuilder();
				for(int j = 0; j < gameWidth; j++)
					configLine.append(config[j + (i * gameWidth)]);
				configLine.append(" ");
				line.append(configLine);
			}
			if(consoleMode) System.out.println(line);
			else writer.println(line);
		}
		if(!consoleMode) writer.close();
	}
	
	public static void main(String[] args)
	{	if(args[0].substring(0,5).toLowerCase().equals("batch"))
			new BatchSolve(args);
		else new Main(args);
	}	
}
