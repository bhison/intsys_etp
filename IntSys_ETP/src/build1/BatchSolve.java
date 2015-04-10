package build1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * A simple class used to create multiple Main objects, each with different puzzle strings
 * as found within the specified batch????.txt file
 * 
 * @author Tim
 * @version 03/04/2015
 */
public class BatchSolve {
	
	/**
	 * Runs an instance of Main for each puzzle in the batch, passing any extra parameters as needed
	 *
	 * @param args The arguments specified by the user - see static main(String[] args) comments
	 * @catch IOException If there is an issue reading from the text file
	 */
	public BatchSolve(String[] args)
	{	
		List<String> puzzles;
		try {
			puzzles = Files.readAllLines(Paths.get(args[0]), StandardCharsets.US_ASCII);
			System.out.println("BATCH: puzzles loaded internally");
			for(String puzzle : puzzles)
			{	System.out.println(puzzle);
				String[] a;
				if(args.length > 1)
						a = new String[]{ puzzle, args[1] };
				else
				{	a = new String[]{ puzzle };
						System.out.println(puzzle + " - begin calculation");
				}
					new Main(a);
			}
		}
		catch (IOException e) {
			System.out.println("IOException: " + e.toString());
			return;
		}
	}
}