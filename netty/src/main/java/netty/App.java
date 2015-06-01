package netty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	Random random = new Random() ;
    	List<Integer> array = new ArrayList<Integer>();
    	for (int i = 0; i < 1000; i++) {
    		array.add(random.nextInt(10000));
		}
    	
    	Collections.sort(array, new Comparator<Integer>() {
    		int count = 0 ;
			public int compare(Integer o1, Integer o2) {
				System.out.println(++count);  
				return o1.compareTo(o2);  
			}
		}); 
    	
    }
}
