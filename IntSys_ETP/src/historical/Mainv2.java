//package historical;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.PriorityQueue;
//
//public class Mainv2 
//{
//	
//	private int gameWidth;
//	private int gameHeight;
//	
//	int depth = 0;
//	
//	private static final boolean OUTPUT_TO_CONSOLE = true; //Set this to false to output as files
//	
//	//public Main(String puzzleString, int gameWidth)
//	public Mainv2(String[] args)
//	{	String puzzleString = args[0];
//		int gameWidth = 3; //Change this to alter default
//		if(args.length > 1) gameWidth = Integer.parseInt(args[1]);
//		this.gameWidth = gameWidth;
//		System.out.println("gameWidth set to: " + gameWidth);
//		int puzzleSize = (puzzleString.length() -5 ) /2;
//		System.out.println("puzzleSize set to: " + puzzleSize);
//		gameHeight =  puzzleSize / gameWidth;
//		System.out.println("gameHeight set to: " + gameHeight);
//		char[] start = puzzleString.substring(0, puzzleSize).toCharArray();
//		char[] target = puzzleString.substring(puzzleSize + 1, puzzleSize * 2 + 1).toCharArray();
//		System.out.println("Puzzle string is: " + puzzleString);
//		System.out.println("start set to: " + new String(start));
//		System.out.println("target set to: " + new String(target));
//		LinkedList<char[]> solutionPath = uniformCost(start,target);
//		if(solutionPath == null) System.out.println("ERROR: Something went wrong! No solution found!");
//		else 
//		{	try	{ createSolutionFile(puzzleString, solutionPath, OUTPUT_TO_CONSOLE); }
//			catch(IOException e) { System.out.println(e); }
//		}
//	}
//	
//	/**
//	 * TODO: Get this to operate correctly. Create "actualDistance" method then link it in as
//	 * main solution method.
//	 * @param start
//	 * @param dest
//	 * @return
//	 */
//	private LinkedList<char[]> uniformCost(char[] start, char[] target)
//	{	LinkedList<char[]> route = new LinkedList<char[]>(); 
//		route.add(start);
//		PriorityQueue pairs = new PriorityQueue();
//		pairs.add(new Pair(0.0, route)); // uniform-cost
////		pairs.add(new Pair(estimateDistance(start, target), route)); // best 
//		while (true)
//		{	//System.out.println(pairs); 		// debug traces
//			if (pairs.size() == 0) return null;		// no solutions exist
//			Pair pair = (Pair) pairs.poll(); 		// retrieve and remove (log)
//			route = pair.getRoute();
//			char[] last = route.getLast();
//			if (Arrays.equals(last, target)) return route;
//			LinkedList<char[]> nextConfigs = getAdjacencies(last);//graph.get(last);
//			for (char[] next : nextConfigs)
//			{	if (!route.contains(next))	//deja vu
//				{	LinkedList<char[]> nextRoute = new LinkedList<char[]>(route); 
//					nextRoute.addLast(next);	
//					int distance = nextRoute.size(); // uniform 
//					if(nextRoute.size() > depth)
//					{	depth = nextRoute.size();
//						System.out.println("New Depth = " + depth);
//					}
//	//				double distance = estimateDistance(next, target); // best-first 
//					pairs.add(new Pair(distance, nextRoute)); // log too
//				}
//			}
//		}
//	}  
//     
//    private LinkedList<char[]> getAdjacencies(char[] config)
//    {	int gapIndex = 999;
//    	for(int i = 0; i < config.length; i++)
//			if(config[i] == '_') 
//			{	gapIndex = i;
//				break; //Might need to check if this is actually doing anything
//			}
//    	LinkedList<String> moveList = getValidMoveList(gapIndex);
//    	LinkedList<char[]> adjacencyList = new LinkedList<char[]>();
//    	for(String direction : moveList)
//    	{	int tileIndex = gapIndex;
//    		if(direction == "up") tileIndex += gameWidth;
//    		else if(direction == "down") tileIndex -= gameWidth;
//    		else if(direction == "left") tileIndex += 1;
//    		else if(direction == "right") tileIndex -= 1;
//    		
//    		char[] newConfig = config.clone();
//    		//System.out.println("getAdjacencies.newCofig set to: " + new String(newConfig));		
//    		newConfig[tileIndex] = '_';
//    		newConfig[gapIndex] = config[tileIndex];
//    		System.out.println("newConfig of: " + new String(newConfig));
//    		adjacencyList.add(newConfig);
//    	}
//    	return adjacencyList;
//    }
//	
//	/**
//	 * Takes a gap index and returns list of valid slides based on game dimensions
//	 * @param gapIndex
//	 * @return List of valid slide directions
//	 */
//	private LinkedList<String> getValidMoveList(int gapIndex)
//	{	LinkedList<String> returnArray = new LinkedList<String>();
//		if(gapIndex < gameWidth); else returnArray.add("down");
//		if(gapIndex >= gameWidth * gameHeight - gameWidth); else returnArray.add("up");
//		int column = gapIndex % gameWidth;
//		if(column == 0); else returnArray.add("right");
//		if(column == gameWidth - 1); else returnArray.add("left");		
//		return returnArray;
//	}
//
//	private void createSolutionFile(String puzzleString, LinkedList<char[]> solutionPath, 
//			boolean consoleMode) throws IOException
//	{	PrintWriter writer = null;
//		if(!consoleMode) writer = new PrintWriter(puzzleString, "UTF-8");
//		for(int i = 0; i < gameHeight; i++)
//		{	StringBuilder line = new StringBuilder();
//			for(char[] config : solutionPath)
//			{	StringBuilder configLine = new StringBuilder();
//				for(int j = 0; j < gameWidth; j++)
//					configLine.append(config[j + (i * gameWidth)]);
//				configLine.append(" ");
//				line.append(configLine);
//			}
//			if(consoleMode) System.out.println(line);
//			else writer.println(line);
//		}
//		if(!consoleMode) writer.close();
//	}
//	
////	public static void main(String[] args)
////	{	if(args[0].substring(0,5).toLowerCase().equals("batch"))
////			new BatchSolve(args);
////		else new Mainv2(args);
////	}	
//}
