package build1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class Main 
{
	
	private int gameWidth; //How wide is the game
	private int gameHeight; //How many rows in the game
	
	int depth = 0; //Counts the current depth of search for console output
	
	private static final int DEFAULT_GAME_WIDTH = 3; // Unless parameter passed  on startup, this is the row width
	private static final boolean OUTPUT_TO_CONSOLE = true; //Set this to false to output as files
	private static final boolean USE_HEURISTIC = true; //True - use best first; False - use uniform cost
	
	/**
	 * 
	 * @param args
	 */
	public Main(String[] args)
	{	String puzzleString = args[0];
		if(args.length > 1) gameWidth = Integer.parseInt(args[1]);
		else gameWidth = DEFAULT_GAME_WIDTH;
		int puzzleSize = (puzzleString.length() -5 ) /2;
		gameHeight =  puzzleSize / gameWidth;
		
		System.out.println("Puzzle string to solve is: " + puzzleString);
		String start = puzzleString.substring(0, puzzleSize);
		String target = puzzleString.substring(puzzleSize + 1, puzzleSize * 2 + 1);
		LinkedList<String> solutionPath;
		solutionPath = USE_HEURISTIC ? aStar(start, target) : uniformCost(start,target);
		
		if(solutionPath == null) System.out.println("ERROR: Something went wrong! No solution found!");
		else 
		{	try	{ createSolutionFile(puzzleString, solutionPath, OUTPUT_TO_CONSOLE); }
			catch(IOException e) { System.out.println(e); }
		}
		System.out.println("Puzzle finished");
	}
	
	/**
	 * 
	 * @param start
	 * @param target
	 * @return
	 */
	private LinkedList<String> aStar(String start, String target)
	{
		LinkedList<String> route = new LinkedList<String>(); 
		route.add(start);
		PriorityQueue<Pair> pairs = new PriorityQueue<Pair>();
		pairs.add(new Pair(estimateDistance(start, target), route)); // A*
		
		int round = 0;																// DELETE ME
		while (true)
		{	
			//round++; System.out.println("Round: " +round); 							// DELETE ME
			//System.out.println(pairs); //Debug traces 								// DELETE ME
			//System.out.println(pairs.size());											// DELETE ME
			if(pairs.size() == 0) return null;  // no solutions exist
			Pair pair = (Pair) pairs.poll(); // retrieve and remove (log)
			route = pair.getRoute();
			String last = route.getLast();
			if(last.equals(target)) return route; // exit loop with solution
			LinkedList<String> nextConfigs = getAdjacencies(last);
			//System.out.println(nextConfigs.size() + " adjacent configurations"); 		// DELETE ME
			for(String next: nextConfigs)
			{	if (route.contains(next)); // deja vu
				else
				{	LinkedList<String> nextRoute = new LinkedList<String>(route);
					nextRoute.addLast(next);
					int distance = nextRoute.size() + estimateDistance(next, target);
					//System.out.println("Route size: " + nextRoute.size());			// DELETE ME
					pairs.add(new Pair(distance, nextRoute));
				}
			}
		}
	}
	
	/**
	 * TODO: If you hit the solution, store current depth and do not go past it again
	 * @param start
	 * @param target
	 * @return
	 */
	private LinkedList<String> bestFirst(String start, String target)
	{	LinkedList<String> route = new LinkedList<String>(); 
		route.add(start);
		PriorityQueue<Pair> pairs = new PriorityQueue<Pair>();
		pairs.add(new Pair(estimateDistance(start, target), route)); 	
		while (true)
		{	
			if (pairs.size() == 0) return null;		// no solutions exist
			Pair pair = (Pair) pairs.poll(); 		// retrieve and remove (log)
			route = pair.getRoute();
			String last = route.getLast();
			if (last.equals(target)) return route;
			LinkedList<String> nextConfigs = getAdjacencies(last);
			
			
			
			for (String next : nextConfigs)
			{	if (route.contains(next)) ; //Do nothing - deja vu check by route;
				else
				{	LinkedList<String> nextRoute = new LinkedList<String>(route); 
					nextRoute.addLast(next);	 
					int distance = estimateDistance(next, target); // best-first
					pairs.add(new Pair(distance, nextRoute)); // log too
				}
			}
		}
	} 
	
	/**
	 * 
	 * @param start
	 * @param target
	 * @return
	 */
	public int estimateDistance(String current, String target)
	{	LinkedList<Integer> tilesOutOfPlace = new LinkedList<Integer>();		
		for(int i = 0; i < current.length(); i++)
			if(current.charAt(i) == '_') ; // Do not calculate distance for gap
			else if(current.charAt(i) == target.charAt(i)) ; //Do nothing
			else tilesOutOfPlace.add(i);
		int returnInt = 0;
		for(Integer tileIndex : tilesOutOfPlace) //This is the top level iteration though all out of place chars
		{	LinkedList<Integer> checkedIndices = new LinkedList<Integer>();
			LinkedList<Integer> indicesToCheck = getNextDistanceIndices(
					new LinkedList<Integer>(Arrays.asList(tileIndex)), checkedIndices);
			int distance = 0;
			boolean stillLookingForMatch = true;
			while(stillLookingForMatch)
			{	distance ++;
				for(Integer index : indicesToCheck)
				{	if(current.charAt(tileIndex) == target.charAt(index))
					{	returnInt += distance;
						stillLookingForMatch = false;
						break;
					}	
				}
				if(stillLookingForMatch)
				{	checkedIndices.addAll(indicesToCheck);
					indicesToCheck = getNextDistanceIndices(indicesToCheck, checkedIndices);
				}
			}
		}
		//System.out.println("Manhatten Distance for current = " + current + ", target = " + target + " : " +returnInt);
		return returnInt;
	}
	
	/**
	 * Gives a list of next distance level indexes of the game board, preventing duplicates in line with alreadyChecked list
	 * @param indicesToCheck List of indices to get adjacent indices for
	 * @param alreadyChecked List of indices which we don't need to check again
	 * @return a list of index integers contained within the next distance tier
	 */
	private LinkedList<Integer> getNextDistanceIndices(LinkedList<Integer> indicesToCheck, LinkedList<Integer> alreadyChecked)
	{	LinkedList<Integer> returnList = new LinkedList<Integer>();
		for(Integer i : indicesToCheck)
		{	LinkedList<String> directions = getValidMoveList(i);
			for(String direction : directions)
			{	int index = getIndexByDirection(i, direction);
				if(alreadyChecked.contains(index)) ;
				else returnList.add(index);
			}
		}
		return returnList;
	}
	
	/**
	 * @param start
	 * @param dest
	 * @return
	 */
	private LinkedList<String> uniformCost(String start, String target)
	{	// Create a hashmap of lists for each gap position - we store calculated configurations by gap location for speed
		Map<Integer, LinkedList<String>> allCalculated = new HashMap<Integer, LinkedList<String>>();
		for(int i = 0; i < start.length(); i++)
			allCalculated.put(i, new LinkedList<String>());
		
		LinkedList<String> route = new LinkedList<String>(); 
		route.add(start);
		PriorityQueue<Pair> pairs = new PriorityQueue<Pair>();
		pairs.add(new Pair(0, route)); // uniform-cost
//		pairs.add(new Pair(estimateDistance(start, target), route)); // best 
		while (true)
		{	//System.out.println(pairs); 		// debug traces
			if (pairs.size() == 0) return null;		// no solutions exist
			Pair pair = (Pair) pairs.poll(); 		// retrieve and remove (log)
			route = pair.getRoute();
			String last = route.getLast();
			if (last.equals(target)) return route;
			LinkedList<String> nextConfigs = getAdjacencies(last);
			for (String next : nextConfigs)
			{	if (route.contains(next)) ; //deja vu by route;
				else if(allCalculated.get(next.indexOf('_')).contains(next)) ; //deja vu by gap/all calculated
				else
				{	allCalculated.get(next.indexOf('_')).add(next);
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
    
	/**
	 * 
	 * @param stringConfig
	 * @return
	 */
    private LinkedList<String> getAdjacencies(String stringConfig)
    {	int gapIndex = stringConfig.indexOf('_');
    	LinkedList<String> moveList = getValidMoveList(gapIndex);
    	LinkedList<String> adjacencyList = new LinkedList<String>();
    	for(String direction : moveList)
    	{	int tileIndex = getIndexByDirection(gapIndex, direction);    		
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
     * 
     * @param startPositon
     * @param direction
     * @return
     */
    private int getIndexByDirection(int startPositon, String direction)
    {	int tileIndex = startPositon;
    	if(direction == "up") tileIndex += gameWidth;
		else if(direction == "down") tileIndex -= gameWidth;
		else if(direction == "left") tileIndex += 1;
		else if(direction == "right") tileIndex -= 1;
    	return tileIndex;
    }
	
	/**
	 * Takes a position index and returns list of valid slides based on game dimensions
	 * @param gapIndex
	 * @return List of valid slide directions
	 */
	private LinkedList<String> getValidMoveList(int positionIndex)
	{	LinkedList<String> returnArray = new LinkedList<String>();
		if(positionIndex < gameWidth); else returnArray.add("down");
		if(positionIndex >= gameWidth * gameHeight - gameWidth); else returnArray.add("up");
		int column = positionIndex % gameWidth;
		if(column == 0); else returnArray.add("right");
		if(column == gameWidth - 1); else returnArray.add("left");		
		return returnArray;
	}

	/**
	 * 
	 * @param puzzleString
	 * @param stringSolutionPath
	 * @param consoleMode
	 * @throws IOException
	 */
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
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{	if(args[0].substring(0,5).toLowerCase().equals("batch"))
			new BatchSolve(args);
		else new Main(args);
	}	
}
