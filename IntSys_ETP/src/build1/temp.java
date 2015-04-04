package build1;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

public class temp {

    private ArrayList<String> configs = new ArrayList<String>();
    private int unique = 0;
    
	@Test
	public void test() {
		//char[] arr = new char[] {'a','a','b','b','b','c','d','d','d','d','d','e'};
		permutation("", "aabbbcddddde");
	}

	private void permutation(String prefix, String str) {
	    int n = str.length();
	    if (n == 0) //System.out.println(prefix);
	    { 
	    	if(!configs.contains(prefix))
	    	{	configs.add(prefix);
	    		unique ++;
	    		System.out.println(unique + ":" + prefix);
	    	}
	    }
	    else {
	        for (int i = 0; i < n; i++)
	            permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
	    }
	}

}
