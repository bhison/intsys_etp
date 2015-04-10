package build1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class does almost all of the work for the slide puzzle solver.
 * 
 * It contains multiple algorithms for solving slide puzzles in optimal ways. To switch between
 * Uniform Cost Search and A* Best First, simply change the USE_HEURISTIC boolean at the top of the class.
 * You also have the options to print to console or print to new files in the root folder, also adjusted
 * by a boolean at the top.
 * 
 * When running the software, the first argument should either be the name of the puzzle you want to create
 * in the format xxxxxxxxxxxx2xxxxxxxxxxxx.txt or, if you want to run multiple puzzles, any .txt suffixed
 * filename which refers to an existing file in the root directory which has one puzzle per line.
 * 
 * You also have the option of a second argument which, if specified, will define an alternative game width
 * the the default as shown below.
 * 
 * @author Tim (Much code taken from Andy King's slides for the Univeristy of Kent's CO528 Intelligent Systems module)
 * @version 09/04/2015
 *
 */
public class Main 
{
	private int gameWidth; //How wide is the game
	private int gameHeight; //How many rows in the game
	
	private static final int DEFAULT_GAME_WIDTH = 3; // Unless parameter passed  on startup, this is the row width
	private static final boolean OUTPUT_TO_CONSOLE = true; //Set this to false to output as files; true console ONLY
	private static final boolean USE_HEURISTIC = true; //True - use best first algo; False - use uniform cost algo
	
	/**
	 * The main class is either called from the static main(String[] args) method or from the
	 * BatchSolve class in the case of a batch of puzzles.
	 * 
	 * @param args Arguments array passed from main(String[] args) method. The first argument should either be
	 * the name of the puzzle solution file you wish to make or the name of the file which contains
	 * the list of puzzles. The second is optional and should be an int that specifies a custom game width
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
		{	try	{ createSolutionFile(puzzleString, solutionPath); }
			catch(IOException e) { System.out.println(e); }
		}
		System.out.println("Puzzle finished in " + solutionPath.size() + " slides.");
	}
	
	/**
	 * This is the fastest solution method which is used when USE_HEURISTIC static variable has
	 * been set to true. This is a best first search which employs a 'Manhattan' heuristic to
	 * greatly improve the speed of finding an optimal solution to the puzzle.
	 * 
	 * @param start The puzzle's starting configuration
	 * @param target The puzzle's final, target configuration
	 * @return A list which represents the shortest sequence of configurations needed to move from
	 * start to finish
	 */
	private LinkedList<String> aStar(String start, String target)
	{
		LinkedList<String> route = new LinkedList<String>(); 
		route.add(start);
		PriorityQueue<Pair> pairs = new PriorityQueue<Pair>();
		pairs.add(new Pair(estimateDistance(start, target), route));
		while (true)
		{	if(pairs.size() == 0) return null;  // no solutions exist
			Pair pair = (Pair) pairs.poll(); // retrieve and remove (log)
			route = pair.getRoute();
			String last = route.getLast();
			if(last.equals(target)) return route; // exit loop with solution
			LinkedList<String> nextConfigs = getAdjacencies(last);
			for(String next: nextConfigs)
			{	if (route.contains(next)); // deja vu (single route only)
				else
				{	LinkedList<String> nextRoute = new LinkedList<String>(route);
					nextRoute.addLast(next);
					int distance = nextRoute.size() + estimateDistance(next, target);
					pairs.add(new Pair(distance, nextRoute));
				}
			}
		}
	}
	
	/**
	 * This is the Manhattan heuristic calculator. It takes a string configuration of current
	 * and one of the target then returns the minimum number of moves it would take to move each
	 * tile in to it's nearest target space if no other tiles were on the game board.
	 * This heuristic is admissible as it will always take more moves that what is returned
	 * to reach a winning configuration.
	 * 
	 * @param current The configuration that needs to be checked for distance from target
	 * @param target The overall target configuration for this puzzle
	 * @return The Manhattan heuristic of theoretical minimum distance
	 */
	public int estimateDistance(String current, String target)
	{	LinkedList<Integer> tilesOutOfPlace = new LinkedList<Integer>();		
		for(int i = 0; i < current.length(); i++)
			if(current.charAt(i) == '_') ; // Do not calculate distance for gap (may or may not be needed???)
			else if(current.charAt(i) == target.charAt(i)) ; //Do nothing
			else tilesOutOfPlace.add(i);
		int returnInt = 0;
		for(Integer tileIndex : tilesOutOfPlace)
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
		return returnInt;
	}
	
	/**
	 * Gives a list of next distance level indexes of the game board, preventing duplicates in line with alreadyChecked list
	 * 
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
     * Returns an index based on starting index and direction of move
     * 
     * @param startPositon The position moving from
     * @param direction The direction to move in
     * @return The resulting index
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
	 * Takes a position index and returns list of valid slides directions based on game dimensions
	 * 
	 * @param positionIndex The index you are checking for
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
	 * Return all adjacent configurations from specified string configuration
	 * 
	 * @param stringConfig The configuration you want to check, as a simple string
	 * @return A list of all possible configurations you can slide to
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
	 * This is the Uniform Cost Search algorithm which can be used as an alternative to the above A* algorithm.
	 * It is also reliable for giving optimal solutions to the puzzles however it takes a LOT longer. This has
	 * however been slightly sped up by having a record of already calculated configurations, split by the gap
	 * index of the config (probably my proudest invention of this project! Woo!).
	 * Note: this algorithm is redundant unless the USE_HEURISTIC static value at the top of the class is set
	 * to false.
	 * 
	 * @param start The start configuration of the puzzle
	 * @param target The target destination for the puzzle
	 * @return A list of the optimal route of configurations between start and target
	 */
	private LinkedList<String> uniformCost(String start, String target)
	{	// Create a hashmap of lists for each gap position - we store calculated configurations by gap location for speed
		Map<Integer, LinkedList<String>> allCalculated = new HashMap<Integer, LinkedList<String>>();
		for(int i = 0; i < start.length(); i++)
			allCalculated.put(i, new LinkedList<String>());
		
		LinkedList<String> route = new LinkedList<String>(); 
		route.add(start);
		PriorityQueue<Pair> pairs = new PriorityQueue<Pair>();
		pairs.add(new Pair(0, route));
		while (true)
		{	if (pairs.size() == 0) return null;		// no solutions exist
			Pair pair = (Pair) pairs.poll(); 		// retrieve and remove (log)
			route = pair.getRoute();
			String last = route.getLast();
			if (last.equals(target)) return route;
			LinkedList<String> nextConfigs = getAdjacencies(last);
			int depth = 0; //Int for storing how deep search has gone
			for (String next : nextConfigs)
			{	if (route.contains(next)) ; //deja vu by route;
				else if(allCalculated.get(next.indexOf('_')).contains(next)) ; //deja vu by all calculated by gap
				else
				{	allCalculated.get(next.indexOf('_')).add(next);
					LinkedList<String> nextRoute = new LinkedList<String>(route); 
					nextRoute.addLast(next);	
					int distance = nextRoute.size(); // uniform 
					pairs.add(new Pair(distance, nextRoute));
					if(nextRoute.size() > depth) //Increase depth and print the fact new step has been reached
					{	depth = nextRoute.size();
						System.out.println("New Depth = " + depth);
					}
				}
			}
		}
	}  
    
	/**
	 * This is used when an optimal solution has been found. Depending on how the OUTPUT_TO_CONSOLE
	 * variable is set, this will either output to a text file (overwriting any existing text file
	 * by the same name) or simply print results to the console.
	 * 
	 * @param puzzleString The original puzzle string. If output is to file, this will be it's name.
	 * @param stringSolutionPath The list of configurations the puzzle was solved via.
	 * @throws IOException If there is a problem with the filewriter
	 */
	private void createSolutionFile(String puzzleString, LinkedList<String> stringSolutionPath) throws IOException
	{	LinkedList<char[]> solutionPath = new LinkedList<char[]>();
		for(String s : stringSolutionPath)
		{	solutionPath.add(s.toCharArray());			
		}
		PrintWriter writer = null;
		if(!OUTPUT_TO_CONSOLE) writer = new PrintWriter(puzzleString, "UTF-8");
		for(int i = 0; i < gameHeight; i++)
		{	StringBuilder line = new StringBuilder();
			for(char[] config : solutionPath)
			{	StringBuilder configLine = new StringBuilder();
				for(int j = 0; j < gameWidth; j++)
					configLine.append(config[j + (i * gameWidth)]);
				configLine.append(" ");
				line.append(configLine);
			}
			if(OUTPUT_TO_CONSOLE) System.out.println(line);
			else writer.println(line);
		}
		if(!OUTPUT_TO_CONSOLE) writer.close();
	}
	
	/**
	 * The static main(String[] args) method. If the first parameter starts with 'batch'
	 * request is sent to a batch object to call multiple instances of Main class.
	 * 
	 * @param args [0] - The filename (either a puzzle string or 'batch....txt'), [1] (optional) Custom game width
	 */
	public static void main(String[] args)
	{	if(args[0].substring(0,5).toLowerCase().equals("batch"))
			new BatchSolve(args);
		else new Main(args);
	}	
}
