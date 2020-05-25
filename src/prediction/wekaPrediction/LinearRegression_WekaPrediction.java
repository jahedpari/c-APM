package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;
import market.MarketMaker;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.LinearRegression;



public class LinearRegression_WekaPrediction extends WekaPrediction
{



	AbstractClassifier classifier =  new LinearRegression();
		
		public LinearRegression_WekaPrediction(MarketMaker mm) {
			
		  super(mm);
		

		  // ((LinearRegression) classifier).setLearningRate(1.0E-4);
	
		  
		 setCalssifier(  classifier);
	}
		
		public void reset() {
			this.allData =  new Vector<Data>();
		                                                                                                                                                                                                                                                                                                                              
			
//			if( U.marketNumber >= 1)
//			  ((SGD) classifier).reset();
			
			
			
		}

}
