package agents;

import java.math.BigDecimal;
import java.util.HashMap;
import tradeStrategy.QlearningRegTradeStaretgy;
import tradeStrategy.ConstantRegTradeStaretgy;
import tradeStrategy.TradeStrategy_Regression;
import market.MarketMaker;
import event.MarketEndedEvent;
import event.MarketStartedEvent;
import event.ResultRevealed;
import event.TradeEvent;
import Utill.U;
import Utill.Utility;

public class RL_RegAgent extends RegressionAgent {

	public static int agentNumber = U.firstColumnNumber - 1;
	double currentPrediction;
	BigDecimal[] marketPrDifference = new BigDecimal[U.days];
	BigDecimal[] marketPr = new BigDecimal[U.days];
	double[] myPrediction = new double[U.days];
	double[] myCapital = new double[U.days];
	double[] myConfidence = new double[U.days];

	public double getCurrentPrediction() {
		return currentPrediction;
	}

	
	//Constructor
	public RL_RegAgent(MarketMaker aMarketMaker, 
			String aTradeStartegyName, double Confidence, String name) {

		super(aMarketMaker,   Confidence);

		agentNumber++;

		setAgentName(name);
		TradeStrategy_Regression aTradeStartegy = null;

		
		// recognize the trade strategy
		if (aTradeStartegyName.trim()
				.equalsIgnoreCase("SimpleRegTradeStaretgy"))
			aTradeStartegy = new ConstantRegTradeStaretgy(aMarketMaker);
		else if (aTradeStartegyName.trim().equalsIgnoreCase(
				"Q-LearnTradeStaretgy"))
			
			
			// set agent's trade strategy
	    aTradeStartegy = new QlearningRegTradeStaretgy(aMarketMaker,
					agentName, agentNumber);

		setTradeStartegy(aTradeStartegy);

	}


	
	
//  agents decide  to trade on each day of a market
	public void onTradeEvent(TradeEvent event) {

		
		//save agent information about this round into history
		marketPr[mm.currentMarketDay - 1] = new BigDecimal(
				mm.getMarketPrediction(), U.mc);
		marketPrDifference[mm.currentMarketDay - 1] = new BigDecimal(
				mm.getMarketPrediction(), U.mc).subtract(new BigDecimal(
				currentPrediction, U.mc));
		myPrediction[mm.currentMarketDay - 1] = currentPrediction;
		myCapital[mm.currentMarketDay - 1] = capital;
		myConfidence[mm.currentMarketDay - 1] = confidence;

		
		// use the trade strategy to decide the basket to for transaction
		HashMap<Double, Double> basket = tradeStartegy.giveBasket(this);

		double maxMoney = U.maxRatePerTransaction * capital;

		
		//reporting purpose
		String output = "";
		// add agent name and basket info
		output += mm.getMarketDate() + U.delim + mm.currentMarketDay + U.delim;
		output += this.getAgentName() + U.delim;
		output += basket.toString() + U.delim;
		// add limit price info
		output += "maxmoney: " + maxMoney + U.delim + "ratePerTransaction: "
				+ U.maxRatePerTransaction;
		output += "\n";
		Utility.WriteToReportT(output);

	
		// agent makes the trade
	      tradeSecurity(basket);
		

	}
	
	

	// agents update their belief once the true outcome is revealed
	public void onResultRevealedEvent(ResultRevealed event) {

		updateParameters();

	}

	


	

	//update agent's believes
	public void updateParameters() {

		double trueAnswer = mm.getWinningValue();
		U.p(this.getAgentName() + "   is updating their parameters");
		
		//update prediction model
		prModel.updateBelief(mm.getWinningValue());

		
		//update confidence
		double percentagError = 1;
		if (trueAnswer != 0)
			percentagError = Math.min(Math.abs(firstDayPrediction - trueAnswer)/ trueAnswer, 1);

		confidence = confidence * (1 - U.confidenceAplpha)
				+ (1 - percentagError) * U.confidenceAplpha;

		 //update trade strategy
		tradeStartegy.updateBelief(previousReward, marketPrDifference,
				myPrediction, myCapital, myConfidence, trueAnswer, marketPr);

	}

	
	
	
	
	// the agent predict its prediction only once (i.e when the market is started)
	public void onMarketStartedEvent(MarketStartedEvent event) {
		super.onMarketStartedEvent(event);

		//reporting purpose
		Utility.WriteToReportQ("Agent:" + this.agentName + U.delim);
 

		//predict the outcome using the model
		firstDayPrediction = prModel.predict(myData, mm.getMarketDate());
		currentPrediction = firstDayPrediction;

	}

	
	
	
	
	public void onMarketEndedEvent(MarketEndedEvent event) {

		super.onMarketEndedEvent(event);

	}
	
	public void run() {

		// System.out.println(this.getClass().getSimpleName()+" :I am alive now ");
	}

}
