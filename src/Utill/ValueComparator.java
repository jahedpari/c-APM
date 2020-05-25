package Utill;

import java.util.Comparator;


 
public class ValueComparator implements Comparator<Double> {

 
	
     
	public int compare(Double o1, Double o2) {
 
		int ans=1;
		
		if(o1<=o2)
			ans=-1;
		
		return ans;
	}

	 

}