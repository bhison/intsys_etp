package build1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BatchSolve {
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