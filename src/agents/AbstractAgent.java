package agents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import prediction.PredictionModel;
import tradeStrategy.TradeStrategy;
import tradeStrategy.TradeStrategy_Regression;
import Data.Data;
import Utill.MyException;
import Utill.SimDate;
import market.MarketMaker;
import event.MarketEndedEvent;
import event.MarketStartedEvent;
import event.Event;
import event.ResultRevealed;
import event.TradeEvent;
import event.newExperimentStartedEvent;

public abstract class AbstractAgent  implements AgentInterface {

 

	String agentName;
	int agentNumber;

	public int getAgentNumber() {
		return agentNumber;
	}

	public void setAgentNumber(int agentNumber) {
		this.agentNumber = agentNumber;
	}

	protected static MarketMaker mm;
	protected double capital;

	protected PredictionModel prModel;
 
	protected TradeStrategy_Regression tradeStartegy;

	protected double confidence;

	protected Data myData;

	protected double previousReward;
	protected double previousRevenue;

	public void updateMarketData(double reward, double revenue) {
		previousReward = reward;
		previousRevenue = revenue;

	}

	public double getPreviousReward() {
		return previousReward;
	}

	public void setPreviousReward(double previousReward) {
		this.previousReward = previousReward;
	}

	public double getPreviousRevenue() {
		return previousRevenue;
	}

	public void setPreviousRevenue(double previousRevenue) {
		this.previousRevenue = previousRevenue;
	}

	public Data getMyData() {
		return myData;
	}

	public void setMyData(Data myData) {
		this.myData = myData;
	}

	protected SimDate currentDate;
 

	// agent performance history. How much loss and revenue received in the previous markets
	protected Vector<Double> rewardPerformance = new Vector<Double>();  
	
	
 

	public AbstractAgent(MarketMaker aMarketMaker, PredictionModel model,
			  double Confidence) {
		super();
		capital = 0;
		mm = aMarketMaker;
		prModel = model;
	 
		currentDate = mm.getToday();
		confidence = Confidence;

	}

	public AbstractAgent(MarketMaker aMarketMaker,
			  double Confidence) {
		super();
		capital = 0;
		mm = aMarketMaker;
		 
		currentDate = mm.getToday();
		confidence = Confidence;

	}

	public AbstractAgent(Vector<Integer> dataIndex, MarketMaker aMarketMaker,
			  double Confidence) {
		super();
		capital = 0;
		mm = aMarketMaker;
 
		currentDate = mm.getToday();
		confidence = Confidence;

	}

	public double tradeSecurity(HashMap<Double, Double> basket) {

		// Check if there is nothing in the basket, then stop shopping
		int counter = 0;
		Iterator<Double> iterator = basket.keySet().iterator();
		while (iterator.hasNext()) {
		  iterator.next();
			counter++;
		}

		if (counter == 0)
			return 0;

		double cost = 0;
		try {
 
			cost = mm.tradeSecurity(this, basket);

		}

		catch (MyException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return cost;
	 

	}

	public void updatePerformance(double reward, boolean correct) {
		rewardPerformance.add(reward);
 
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}

	public void addCapital(double amount) {
		this.capital = capital + amount;
	}

	public void reduceCapital(double amount) {
		this.capital = capital - amount;
	}

	public void initialise() {
	}

	public void run() {
	}

 
	public void eventOccurred(Event event) {

		try {

			if (event instanceof TradeEvent) {
				onTradeEvent((TradeEvent) event);
			}
			else if (event instanceof MarketStartedEvent) {
				onMarketStartedEvent((MarketStartedEvent) event);
			} else if (event instanceof ResultRevealed) {
				onResultRevealedEvent((ResultRevealed) event);
			} else if (event instanceof MarketEndedEvent) {
				onMarketEndedEvent((MarketEndedEvent) event);
			} else if (event instanceof newExperimentStartedEvent) {
				OnNewExperimentStartedEvent((newExperimentStartedEvent) event);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Child agents must implement this function
	public abstract void onTradeEvent(TradeEvent event);

	public void OnNewExperimentStartedEvent(newExperimentStartedEvent event) {
		prModel.reset();
		tradeStartegy.reset();

		rewardPerformance = new Vector<Double>();
 
	}

	public void onMarketEndedEvent(MarketEndedEvent event) {

	}

	public void onResultRevealedEvent(ResultRevealed event) {

	}

	public void onMarketStartedEvent(MarketStartedEvent event) {

	 
		tradeStartegy.reset();

	}

	public void printData() {
		String output = "";
 

		System.out.println(output);
	}

	public void unsubscribeFromEvents() {
	 
	}

	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public String getAgentName() {
		return agentName;

	}

	public void setAgentName(String name) {
		agentName = name;
	}

	public PredictionModel getPrModel() {
		return prModel;
	}

	public void setPrModel(PredictionModel prModel) {
		this.prModel = prModel;
	}
 

	public TradeStrategy getTradeStartegy() {
		return tradeStartegy;
	}

	public void setTradeStartegy(TradeStrategy_Regression tradeStartegy) {
		this.tradeStartegy = tradeStartegy;
	}

}