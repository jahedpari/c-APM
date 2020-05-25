package agents;

import java.util.HashMap;
import java.util.Vector;
 

 
 
import tradeStrategy.ConstantRegTradeStaretgy;
import tradeStrategy.TradeStrategy_Regression;
 
import market.MarketMaker;
import event.MarketEndedEvent;
import event.MarketStartedEvent;
import event.ResultRevealed;
import event.TradeEvent;
import Utill.U;
import Utill.Utility;

public class RegressionAgent extends AbstractAgent {

	double currentPrediction;
	double firstDayPrediction;


   //constructor
	public RegressionAgent(MarketMaker aMarketMaker,  
			String aTradeStartegyName, double Confidence) {

		super(aMarketMaker,   Confidence);

		TradeStrategy_Regression aTradeStartegy = null;

		if (aTradeStartegyName.trim()
				.equalsIgnoreCase("SimpleRegTradeStaretgy"))
			aTradeStartegy = new ConstantRegTradeStaretgy(aMarketMaker);

		setTradeStartegy(aTradeStartegy);

	}
	//constructor
	public RegressionAgent(Vector<Integer> dataIndex, MarketMaker aMarketMaker,
			  String aTradeStartegyName,
			double Confidence) {

		super(dataIndex, aMarketMaker,   Confidence);

		TradeStrategy_Regression aTradeStartegy = null;

		if (aTradeStartegyName.trim()
				.equalsIgnoreCase("SimpleRegTradeStaretgy"))
			aTradeStartegy = new ConstantRegTradeStaretgy(aMarketMaker);

	 
		setTradeStartegy(aTradeStartegy);

	}
	//constructor
	public RegressionAgent(MarketMaker aMarketMaker,  
			double confidence) {
		super(aMarketMaker,  confidence);

	}
	//constructor
	public RegressionAgent(Vector<Integer> dataIndex, MarketMaker aMarketMaker,
			  double confidence) {
		super(dataIndex, aMarketMaker,   confidence);
	}


	public void onResultRevealedEvent(ResultRevealed event) {

		super.onResultRevealedEvent(event);
		updateParameters();

	}

	
	//update agent belief
	public void updateParameters() {
		double trueAnswer = mm.getWinningValue();
		prModel.updateBelief(trueAnswer);

		confidence = 1 - Math.min(Math.abs(firstDayPrediction - trueAnswer)
				/ trueAnswer, 1);

		//U.p("percentage error: " + Math.abs(firstDayPrediction - trueAnswer)/ trueAnswer);
		//U.p("new confidence: " + confidence);

	}

	// the agent predict it only once, when the market is started
	public void onMarketStartedEvent(MarketStartedEvent event) {
		super.onMarketStartedEvent(event);



		Utility.WriteToReportQ("Agent:" + this.agentName + U.delim);

		firstDayPrediction = prModel.predict(myData, mm.getMarketDate());
		currentPrediction = firstDayPrediction;

	}

	// here is what your agent decides to trade on each day of a market
	public void onTradeEvent(TradeEvent event) {

		HashMap<Double, Double> basket = tradeStartegy.giveBasket(this);

		double maxMoney = U.maxRatePerTransaction * capital;

		String output = "";

		// add agent name and basket info
		output += mm.getMarketDate() + U.delim + mm.currentMarketDay + U.delim;
		;
		output += this.getAgentName() + U.delim;
		output += basket.toString() + U.delim;

		output += "maxmoney: " + maxMoney + U.delim + "ratePerTransaction: "
				+ U.maxRatePerTransaction;

		output += "\n";

		Utility.WriteToReportT(output);

		double cost = tradeSecurity(basket);
		maxMoney = maxMoney - cost;

	}
	
	public void run() {

		// System.out.println(this.getClass().getSimpleName()+" :I am alive now ");
	}
	

	public void onMarketEndedEvent(MarketEndedEvent event) {

		super.onMarketEndedEvent(event);

	}

	public double getCurrentPrediction() {
		return currentPrediction;
	}

	public void setCurrentPrediction(double currentPrediction) {
		this.currentPrediction = currentPrediction;
	}

}
