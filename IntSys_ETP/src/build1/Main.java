package build1;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class Main 
{
	
	private int gameWidth;
	private int gameHeight;
	
	private static final boolean OUTPUT_TO_CONSOLE = false; //Set this to false to output as files
	
	//public Main(String puzzleString, int gameWidth)
	public Main(String[] args)
	{	String puzzleString = args[0];
		int gameWidth = 3; //Change this to alter default
		if(args.length > 1) gameWidth = Integer.parseInt(args[1]);
		this.gameWidth = gameWidth;
		System.out.println("gameWidth set to: " + gameWidth);
		int puzzleSize = (puzzleString.length() -5 ) /2;
		System.out.println("puzzleSize set to: " + puzzleSize);
		gameHeight =  puzzleSize / gameWidth;
		System.out.println("gameHeight set to: " + gameHeight);
		char[] start = puzzleString.substring(0, puzzleSize).toCharArray();
		char[] target = puzzleString.substring(puzzleSize + 1, puzzleSize * 2 + 1).toCharArray();
		System.out.println("Puzzle string is: " + puzzleString);
		System.out.println("start set to: " + new String(start));
		System.out.println("target set to: " + new String(target));
		LinkedList<char[]> solutionPath = iterativeDeepening(start,target);
		if(solutionPath == null) System.out.println("ERROR: Something went wrong! No solution found!");
		else 
		{	try	{ createSolutionFile(puzzleString, solutionPath, OUTPUT_TO_CONSOLE); }
			catch(IOException e) { System.out.println(e); }
		}
	}
	
	private LinkedList<char[]> iterativeDeepening(char[] start, char[] dest)
	{	for (int depth = 1; true; depth++) // doubtful termination
		{	System.out.println("******************************************** DEPTH = " + depth + " *******");
			LinkedList<char[]> route = new LinkedList<char[]>(Arrays.asList(start));
			route = depthFirst(route, dest, depth);
			if (route != null) return route; // fast exit
		}
	}
	
//    private LinkedList<char[]> depthFirst(char[] start, char[] dest, int depth) 
//    {  	if (depth == 0) return null;
//		else if (Arrays.equals(start, dest))
//		{	LinkedList<char[]> route = new LinkedList<char[]>(); 
//			route.add(dest); // construct singleton route
//			return route;	
//		}
//		else 
//		{	LinkedList<char[]> nextConfigs = getAdjacencies(start); 
//			for (char[] next : nextConfigs) // search top-down
//			{	LinkedList<char[]> route = depthFirst(next, dest, depth - 1);
//				if (route != null)
//				{	route.addFirst(start);
//					return route;	
//				}
//			}
//			return null; 
//		}
//    }
    
    
	private LinkedList<char[]> depthFirst(LinkedList<char[]> route, char[] dest, int depth)
    {  	if (depth == 0) return null;
		char[] last = route.getLast();
		//System.out.println("Last: " + new String(last));
		//System.out.println("Dest: " + new String(dest));
		if (new String(last).equals(new String(dest))) 
		{ 	System.out.println("Last = Dest"); 
			return route; 
			}
		else
		{	LinkedList<char[]> nextConfigs = getAdjacencies(route.getLast());
			for (char[] next:nextConfigs)
			{
				if (!route.contains(next))
				{
					LinkedList<char[]> nextRoute = (LinkedList<char[]>) route.clone();
					nextRoute.add(next);
					LinkedList<char[]> wholeRoute =
						depthFirst(nextRoute, dest, depth - 1); 
					if (wholeRoute != null) return wholeRoute;
				}
			}
		}
		return null;
	}  
    
    
    
    
    
    
    
    private LinkedList<char[]> getAdjacencies(char[] config)
    {	int gapIndex = 999;
    	for(int i = 0; i < config.length; i++)
			if(config[i] == '_') 
			{	gapIndex = i;
				break; //Might need to check if this is actually doing anything
			}
    	LinkedList<String> moveList = getValidMoveList(gapIndex);
    	LinkedList<char[]> adjacencyList = new LinkedList<char[]>();
    	for(String direction : moveList)
    	{	int tileIndex = gapIndex;
    		if(direction == "up") tileIndex += gameWidth;
    		else if(direction == "down") tileIndex -= gameWidth;
    		else if(direction == "left") tileIndex += 1;
    		else if(direction == "right") tileIndex -= 1;
    		
    		char[] newConfig = config.clone();
    		//System.out.println("getAdjacencies.newCofig set to: " + new String(newConfig));
    		char tileValue = newConfig[tileIndex];
    		
    		newConfig[tileIndex] = '_';
    		newConfig[gapIndex] = tileValue;
    		//System.out.println("newConfig of: " + new String(newConfig));
    		adjacencyList.add(newConfig);
    	}
    	return adjacencyList;
    }
	
	/**
	 * Takes a gap index and returns list of valid slides based on game dimensions
	 * @param gapIndex
	 * @return List of valid slide directions
	 */
	private LinkedList<String> getValidMoveList(int gapIndex)
	{	LinkedList<String> returnArray = new LinkedList<String>(Arrays.asList("up", "right", "down", "left"));
		if(gapIndex < gameWidth) returnArray.remove("down");
		if(gapIndex >= gameWidth * gameHeight - gameWidth) returnArray.remove("up");
		int column = gapIndex % gameWidth;
		if(column == 0) returnArray.remove("right");
		if(column == gameWidth - 1) returnArray.remove("left");
		
		//Delete this after debug
		//System.out.println("Valid moves for gapIndex " + gapIndex + ":");
//		for(String direction : returnArray)
//			System.out.println(" >" + direction);
//			
		return returnArray;
	}

	private void createSolutionFile(String puzzleString, LinkedList<char[]> solutionPath, 
			boolean consoleMode) throws IOException
	{	PrintWriter writer = null;
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
