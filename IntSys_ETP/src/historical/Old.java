package historical;

import java.util.LinkedList;

/**
 * An algorithm for solving 3 x 4 sliding tile puzzles
 * It takes a string on the main argument in the style xxxxxxxxxxxx2xxxxxxxxxxxx.txt
 * and then looks to see how the configuration to the left of the 2 can be
 * progressed to the configuration to the right of the 2.
 * 
 * When solved, it outputs step by step details of how to solve the puzzle in to
 * a text file with the filename of the argument passed through initially.
 * 
 * This version is OPTIMAL but not EFFICIENT/A*, in that it does not apply a heuristic
 * of any form, instead simply investigating all possible routes in line with
 * current solution
 * 
 * @author Tim
 *
 */
public class Old {
	
	private char[] startingConfig;
	private char[] solutionConfig;
	
	private char[] activeConfig; //Which config is being actively analysed
	
	/**
	 * Starts the solving algorithm
	 * @param filename Also contains the start configuration [0:11] and target configuration [13:26]
	 */
	public Old(String filename)
	{	startingConfig = filename.substring(0, 12).toCharArray();
		solutionConfig = filename.substring(14, 26).toCharArray();
		LinkedList<TileConfiguration> route = iterativeDeepening();
	}
	
	public LinkedList<TileConfiguration> iterativeDeepening()
	{
		return null;
	}
	
	public void plumb()
	{	
	}
	

	
	/**
	 * Slides a tile in the activeConfig
	 * @param direction The direction to slide the tile (not slide FROM, physically slide!)
	 * @param gapIndex The starting position of the gap in the configuration
	 */
	public void slideTile(String direction, int gapIndex)
	{	int tileIndex = gapIndex;
		if(direction == "u") tileIndex += 3;
		else if(direction == "d") tileIndex -= 3;
		else if(direction == "l") tileIndex -= 1;
		else if(direction == "r") tileIndex += 1;
		else System.out.println("ERROR: Invalid direction passed to slideTile: " + direction);
		
		char tileValue = activeConfig[tileIndex];
		activeConfig[tileIndex] = '_';
		activeConfig[gapIndex] = tileValue;
	}
	
	
	
	//TEST PUSH	
	/*
	 * PLAN: Just going to use whatever works, not worry about efficiency
	 * 
	 * Methods:
	 * > Read text file title & contents, based on argument
	 * at command line; store values as start/solution
	 * > Generate adjacent configurations - String in, String[] out
		 * private LinkedList<String> nextConfigs(String config)
		 *	{ // 1
		 *	 String[] wheels = new String[2];
		 *	 wheels[0] = config.substring(0, wheel);
		 *	 wheels[1] = config.substring(wheel);
		 *	 LinkedList<String> result = new LinkedList<String>();
		 *	 for (int i = 0; i < 2; i++)
		 *	 { // 2
		 *	for (int j = 1; j < wheel; j++)
		 *	{ // 3
		 *	 char[] shiftedWheel = new char[wheel];
		 *	 for (int k = 0; k < wheel; k++)
		 *	 shiftedWheel[k] = wheels[i].charAt((k+j) % wheel);
		 *	 String nextConfig; 
	 *==Find index of '_' then ROUNDUP((index+1)/3,0) for row collection
	 *==Work in arrays or strings? Arrays are easier!
	 */

	
//	public static void main(String[] args) 
//	{	new Old(args[0]);
//	}

}





//
//package build1;
////This may end up being useful: new ArrayList<Element>(Arrays.asList(array))
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//
//import org.jfree.data.xy.XYSeries;
//import org.jfree.data.xy.XYSeriesCollection;
//
//public class DatLoader {
//	
//	private Double[][] dataList;
//	private Double[] xValues;
//	private Double[] yValues;
//	private XYSeries xySeries;
//	private XYSeriesCollection xySeriesCollection;
//	private int dataSize;
//
//	public DatLoader() {
//		List<String> datFile;
//		try {
//			datFile = Files.readAllLines(Paths.get("datfile.dat"), StandardCharsets.US_ASCII);
//			System.out.println("loadDat: Data loaded internally");
//		}
//		catch (IOException e) {
//			System.out.println("IOException: " + e.toString());
//			return;
//		}
//		
//		dataSize = datFile.size();
//		
//		dataList = new Double[dataSize][2];
//		xValues = new Double[dataSize];
//		yValues = new Double[dataSize];
//		int i = 0;
//		for (String line : datFile) {
//		    String[] array = line.split("\\s+");
//		    xValues[i] = Double.parseDouble(array[0]);
//		    yValues[i] = Double.parseDouble(array[1]);
//		    dataList[i] = new Double[]{xValues[i], yValues[i]};
//		    i++;
//		}
//		
//		xySeries = createSeries(dataList);
//		xySeriesCollection = new XYSeriesCollection(xySeries);
//	}
//	
//	private XYSeries createSeries(Double[][] data){
//		XYSeries returnSeries = new XYSeries("TargetCurve");
//		for(Double[] entry : data){
//			returnSeries.add(entry[0], entry[1]);
//		}
//		return returnSeries;
//	}
//	
//	public Double[][] getDataList(){
//		return dataList;
//	}
//	
//	public XYSeries getXYSeries(){
//		return xySeries;
//	}
//	
//	public XYSeriesCollection getXYSeriesCollection(){
//		return xySeriesCollection;
//	}
//	
//	public Double[] getXValues(){
//		return xValues;
//	}
//	
//	public Double[] getYValues(){
//		return yValues;
//	}
//	
//	public int getDataSize(){
//		return dataSize;
//	}
//	
//}





//package build1;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Arrays;
//import java.util.LinkedList;
//import java.util.PriorityQueue;
//
//public class Main 
//{
//	
//	private int gameWidth;
//	private int gameHeight;
//	
//	private int depth = 0;
//	private char[] config; //Temporary config
//	
//	private static final boolean OUTPUT_TO_CONSOLE = true; //Set this to false to output as files
//	
//	//public Main(String puzzleString, int gameWidth)
//	public Main(String[] args)
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
////    private LinkedList<char[]> depthFirst(char[] start, char[] dest, int depth) 
////    {  	if (depth == 0) return null;
////		else if (Arrays.equals(start, dest))
////		{	LinkedList<char[]> route = new LinkedList<char[]>(); 
////			route.add(dest); // construct singleton route
////			return route;	
////		}
////		else 
////		{	LinkedList<char[]> nextConfigs = getAdjacencies(start); 
////			for (char[] next : nextConfigs) // search top-down
////			{	LinkedList<char[]> route = depthFirst(next, dest, depth - 1);
////				if (route != null)
////				{	route.addFirst(start);
////					return route;	
////				}
////			}
////			return null; 
////		}
////    }
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
//	private LinkedList<char[]> iterativeDeepening(char[] start, char[] dest)
//	{	for (int depth = 1; true; depth++) // doubtful termination
//		{	System.out.println("******************************************** DEPTH = " + depth + " *******");
//			LinkedList<char[]> route = new LinkedList<char[]>(Arrays.asList(start));
//			route = depthFirst(route, dest, depth);
//			if (route != null) return route; // fast exit
//		}
//	}
//    
//	private LinkedList<char[]> depthFirst(LinkedList<char[]> route, char[] dest, int depth)
//    {  	if (depth == 0) return null;
//		char[] last = route.getLast();
//		//System.out.println("Last: " + new String(last));
//		//System.out.println("Dest: " + new String(dest));
//		if (new String(last).equals(new String(dest))) 
//		{ 	System.out.println("Last = Dest"); 
//			return route; 
//			}
//		else
//		{	LinkedList<char[]> nextConfigs = getAdjacencies(route.getLast());
//			for (char[] next:nextConfigs)
//			{
//				if (!route.contains(next))
//				{
//					LinkedList<char[]> nextRoute = (LinkedList<char[]>) route.clone();
//					nextRoute.add(next);
//					LinkedList<char[]> wholeRoute =
//						depthFirst(nextRoute, dest, depth - 1); 
//					if (wholeRoute != null) return wholeRoute;
//				}
//			}
//		}
//		return null;
//	}  
//    
//    
//    
//    
//    
//    
//    
//    private LinkedList<char[]> getAdjacencies(char[] oldConfig)
//    {	int gapIndex = 999;
//    	config = oldConfig;
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
//    		//System.out.println("getAdjacencies.newCofig set to: " + new String(newConfig));
//    		char tileValue = config[tileIndex];
//    		
//    		config[tileIndex] = '_';
//    		config[gapIndex] = tileValue;
//    		System.out.println("newConfig of: " + config.toString());
//    		adjacencyList.add(config);
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
//		
//		//Delete this after debug
//		//System.out.println("Valid moves for gapIndex " + gapIndex + ":");
////		for(String direction : returnArray)
////			System.out.println(" >" + direction);
////			
//		return returnArray;
//	}
//	
////	/**
////	 * Takes a gap index and returns list of valid slides based on game dimensions
////	 * @param gapIndex
////	 * @return List of valid slide directions
////	 */
////	private LinkedList<String> getValidMoveList(int gapIndex)
////	{	LinkedList<String> returnArray = new LinkedList<String>(Arrays.asList("up", "right", "down", "left"));
////		if(gapIndex < gameWidth); else returnArray.add("down");
////		if(gapIndex >= gameWidth * gameHeight - gameWidth); else returnArray.add("up");
////		int column = gapIndex % gameWidth;
////		if(column == 0); else returnArray.add("right");
////		if(column == gameWidth - 1); else returnArray.add("left");
////		
////		//Delete this after debug
////		//System.out.println("Valid moves for gapIndex " + gapIndex + ":");
//////		for(String direction : returnArray)
//////			System.out.println(" >" + direction);
//////			
////		return returnArray;
////	}
//	
//	
//	
////	/**
////	 * Takes a gap index and returns list of valid slides based on game dimensions
////	 * @param gapIndex
////	 * @return List of valid slide directions
////	 */
////	private LinkedList<String> getValidMoveList(int gapIndex)
////	{	LinkedList<String> returnArray = new LinkedList<String>(Arrays.asList("up", "right", "down", "left"));
////		if(gapIndex < gameWidth) returnArray.remove("down");
////		if(gapIndex >= gameWidth * gameHeight - gameWidth) returnArray.remove("up");
////		int column = gapIndex % gameWidth;
////		if(column == 0) returnArray.remove("right");
////		if(column == gameWidth - 1) returnArray.remove("left");
////		
////		//Delete this after debug
////		//System.out.println("Valid moves for gapIndex " + gapIndex + ":");
//////		for(String direction : returnArray)
//////			System.out.println(" >" + direction);
//////			
////		return returnArray;
////	}
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
//	public static void main(String[] args)
//	{	if(args[0].substring(0,5).toLowerCase().equals("batch"))
//			new BatchSolve(args);
//		else new Main(args);
//	}	
//}
