package tradeStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Random;
import Utill.U;
import agents.RegressionAgent;
import market.MarketMaker;



public abstract class TradeStrategy_Regression extends TradeStrategy {

protected static MarketMaker marketMaker;
	
    protected Random randomGenerator = new Random();

    
 
    
     TradeStrategy_Regression(MarketMaker marketMaker)
	{
    	 TradeStrategy_Regression.marketMaker=marketMaker;

	}
	

 	public void reset()
		{
 		
 
 		
		}
 
 
 	public  HashMap<Double, Double> giveBasket(RegressionAgent agent) 	
 	{ U.p("we are in TradeStrategy_R:");
	return null;}


 	public void updateBelief(double previousReward, BigDecimal[] marketPrDifference,double [] myPrediction, double[] myCapital, double[] myConfidence, double correctAns,  BigDecimal[] marketPr) 
	{
		 
			
	}

}
