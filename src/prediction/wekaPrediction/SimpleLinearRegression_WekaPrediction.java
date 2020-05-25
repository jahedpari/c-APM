package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.SimpleLinearRegression;


public class SimpleLinearRegression_WekaPrediction extends WekaPrediction {

	
	//AbstractClassifier classifier =  new IsotonicRegression();
	AbstractClassifier classifier =  new SimpleLinearRegression() ;

	public SimpleLinearRegression_WekaPrediction(MarketMaker mm) {
		
		  super(mm);
	
		  
		  
		  setCalssifier(  classifier);
	}
	
	
	
	public void reset() {
		this.allData =  new Vector<Data>();
	                                                                                                                                                                                                                                                                                                                              
		
	}
	
	
	

}


