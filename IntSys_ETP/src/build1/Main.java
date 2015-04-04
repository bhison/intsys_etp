package build1;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

public class Main 
{
	
	private int gameWidth;
	private int gameHeight;
	
	public Main(String puzzleString, int gameWidth)
	{	this.gameWidth = gameWidth;
		int puzzleSize = (puzzleString.length() -5 ) /2;
		gameHeight =  puzzleSize /gameWidth;
		char[] start = puzzleString.substring(0, puzzleSize).toCharArray();
		char[] target = puzzleString.substring(puzzleSize + 1, puzzleSize * 2 + 1).toCharArray();
		LinkedList<char[]> solutionPath = iterativeDeepening(start,target);
		if(solutionPath == null) System.out.println("ERROR: Something went wrong! No solution found!");
		else createSolutionFile(puzzleString, solutionPath);
	}
	
	private LinkedList<char[]> iterativeDeepening(char[] start, char[] dest)
	{	for (int depth = 1; true; depth++) // doubtful termination
		{	LinkedList<char[]> route = depthFirst(start, dest, depth);
			if (route != null) return route; // fast exit
		}
	}
	
    private LinkedList<char[]> depthFirst(char[] start, char[] dest, int depth) 
    {  	if (depth == 0) return null;
		else if (start.equals(dest))
		{	LinkedList<char[]> route = new LinkedList<char[]>(); 
			route.add(dest); // construct singleton route
			return route;	
		}
		else 
		{	LinkedList<char[]> nextConfigs = getAdjacencies(start); 
			for (char[] next : nextConfigs) // search top-down
			{	LinkedList<char[]> route = depthFirst(next, dest, depth - 1);
				if (route != null)
				{	route.addFirst(start);
					return route;	
				}
			}
			return null; 
		}
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
    	{	
    		int tileIndex = gapIndex;
    		if(direction == "up") tileIndex += gameWidth;
    		else if(direction == "down") tileIndex -= gameWidth;
    		else if(direction == "left") tileIndex -= 1;
    		else if(direction == "right") tileIndex += 1;
    		
    		char[] newConfig = config.clone();
    		char tileValue = newConfig[tileIndex];
    		newConfig[tileIndex] = '_';
    		newConfig[gapIndex] = tileValue;	
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
		return returnArray;
	}

	private void createSolutionFile(String puzzleString, LinkedList<char[]> solutionPath)
	{	PrintWriter writer = new PrintWriter(puzzleString, "UTF-8");
		for(int i = 0; i < gameHeight; i++)
		{	StringBuilder line = new StringBuilder();
			for(char[] config : solutionPath)
			{	StringBuilder configLine = new StringBuilder();
				for(int j = 0; j < gameWidth; j++)
					configLine.append(config[j + (i * gameWidth)]);
				configLine.append(" ");
				line.append(configLine);
			}
			writer.println(line);
		}
		writer.close();
		
		
		
		//Create a file in same place as jar with filename of puzzleString
		//RULES:
		// > Should only write to first four lines
		// > Should use spaces as delimiters
		// > Should be in style:
		//
		// 		aaa aaa aaa aaa aaa aaa aaa
		// 		aaa aaa aaa aaa aaa aaa aaa
		// 		aaa aaa aaa aaa aaa aaa aaa
		// 		aaa aaa aaa aaa aaa aaa aaa
		// 
		// > Start should be on left, solution on right with intermediate configurations in between
	}
	
	public static void main(String[] args)
	{	int gameWidth = 3; //Change this to alter default		
		if(args.length > 1) gameWidth = Integer.parseInt(args[1]);
		if((args[0].length() - 5) / 2 % gameWidth == 0) new Main(args[0], gameWidth);
		else System.out.println("ERROR: Invalid game width/puzzle size combo");
	}	

}
