package historical;

import java.util.LinkedList;

public class TileConfiguration {
	char[] characterArray;
	int depth;
	
	TileConfiguration up = null, down = null, left = null, right = null;
	
	//An array for looking up valid directions to slide given any gap position
	private static String[][] validDirections = new String[][] 
	{	new String[] {"up", "left"},
		new String[] {"up", "left", "right"},
		new String[] {"up", "right"},
		new String[] {"up", "down", "left"},
		new String[] {"up", "down", "left", "right"},
		new String[] {"up", "down", "right"},
		new String[] {"up", "down", "left"},
		new String[] {"up", "down", "left", "right"},
		new String[] {"up", "down", "right"},
		new String[] {"down", "left"},
		new String[] {"down", "left", "right"},
		new String[] {"down", "right"}
	};
	
	/**
	 * Constructor for making initial TileConfiguration
	 * @param characterArray
	 * @param depth
	 */
	public TileConfiguration(char[] characterArray, int depth)
	{ 	this.characterArray = characterArray;
		this.depth = depth;
	}
	
	/**
	 * Standard constructor
	 * @param characterArray
	 * @param depth
	 * @param lastSlide
	 * @param parentConfig
	 */
	public TileConfiguration(char[] characterArray, int depth, String lastSlide, TileConfiguration parentConfig)
	{	
		if(lastSlide == null || parentConfig == null); //Do nothing
		else setAdjacency(lastSlide, parentConfig);
		
		this.characterArray = characterArray;
		this.depth = depth;
	}
	
	/**
	 * Calculates and stores all adjacencies. Invalid directions remain null.
	 * Note: can break at first calculation for depth first, then only calculate nulls.
	 */
	public void calculateAdjacencies()
	{	for(String move : validDirections[getGapIndex()])
		{	
			TileConfiguration newConfig = generateNewConfig(move);
			setAdjacency(move, newConfig);
		}
	}
	
	/**
	 * Generates a character array as a mutation of this one, given the slide direction
	 * Does not protect against invalid slides, this should be done before calling this function
	 * @param slideDirection The way a tile should slide to cover gap
	 * @return The new character config
	 */
	private TileConfiguration generateNewConfig(String slideDirection)
	{	
		int gapIndex = 13;
		for(int i = 0; i < characterArray.length; i++)
			if(characterArray[i] == '_') {	gapIndex = i; break; }

		int tileIndex = gapIndex;
		if(slideDirection == "up") tileIndex += 3;
		else if(slideDirection == "down") tileIndex -= 3;
		else if(slideDirection == "left") tileIndex -= 1;
		else if(slideDirection == "right") tileIndex += 1;
		
		char[] returnArray = characterArray.clone();
		char tileValue = returnArray[tileIndex];
		returnArray[tileIndex] = '_';
		returnArray[gapIndex] = tileValue;
		
		return new TileConfiguration(returnArray, depth + 1, slideDirection, this);
	}
	
	/**
	 * Returns the index of the gap in this configuration
	 * @return index of gap
	 */
	public int getGapIndex()
	{	for(int i = 0; i < characterArray.length; i++)
			if(characterArray[i] == '_') return i;

		System.out.println("ERROR: No '_' was found in the configuration array!");
		return 9999;
	}
		
	/**
	 * Sets an adjacency for this configuration
	 * Uses reverse direction to express position in graph rather than effect from slide
	 * @param reverseDirection
	 * @param config
	 */
	private void setAdjacency(String reverseDirection, TileConfiguration adjacentConfig)
	{	if(reverseDirection == "up") down = adjacentConfig;
		else if(reverseDirection == "down") up = adjacentConfig;
		else if(reverseDirection == "left") right = adjacentConfig;
		else if(reverseDirection == "right") left = adjacentConfig;
	}
	
	/**
	 * Returns and adjacency for this configuration
	 * Uses reverse direction to express position in graph rather than effect from slide
	 * @param reverseDirection
	 * @return An adjacent configuration
	 */
	public TileConfiguration getAdjacency(String reverseDirection)
	{	if(reverseDirection == "up") return down;
		else if(reverseDirection == "down") return up;
		else if(reverseDirection == "left") return right;
		else if(reverseDirection == "right") return left;
		return null;
	}

}
