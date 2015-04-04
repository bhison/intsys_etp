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