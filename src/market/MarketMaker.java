package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import event.Event;
import event.MarketEndedEvent;
import event.MarketStartedEvent;
import event.ResultRevealed;
import event.TradeEvent;
import event.newExperimentStartedEvent;
import Utill.Bid;
import Utill.MyException;
import Utill.SimDate;
import Utill.U;
import Utill.Utility;
import agents.AbstractAgent;

public class MarketMaker {

	public int currentMarketDay;

	static Vector<AbstractAgent> participants;
	static int autoCutoffIndex = 0;

	public int PredictionMethod;

	HashMap<AbstractAgent, ArrayList<Bid>> SecurityHolders;

	private double winnerValue;

	private double marketPrediction = 0;

 
	public double previousTreshold;
 

 	Vector<Vector<Double>> agentsErrorSumStackVec1 = new Vector<Vector<Double>>();
	 

	// gives the revenue for each agent in this market
	private String revenueString;
	private String errorString;
	private String investmentString;
	 

	SimDate marketDate;

	SimDate today;

	public boolean isMarketActive;
	
	

	public MarketMaker() {
		participants = new Vector<AbstractAgent>();  

		SecurityHolders = new HashMap<AbstractAgent, ArrayList<Bid>>();

		this.PredictionMethod = U.marketIntegrationMethod;

		isMarketActive = false;
		currentMarketDay = 0;
		revenueString = "";
		errorString = "";


 

	}

	public MarketMaker(Vector<AbstractAgent> traders) {
		participants = traders;

		SecurityHolders = new HashMap<AbstractAgent, ArrayList<Bid>>();

		this.PredictionMethod = U.marketIntegrationMethod;

		isMarketActive = false;
		revenueString = "";
		errorString = "";
		

 
	}
	
	
	// start market
	public void startMarket() {

		isMarketActive = true;
		System.out
				.println("*****************************market Started for DATE: "
						+ marketDate);

		showStatus();
		
		// notify agents that a new market is started
		fireEvent(new MarketStartedEvent());
 
		// notify agents to  trade
		fireEvent_MarketTrade(new TradeEvent());

	}
	
	
	
	

	// notify each agent to trade
	public void fireEvent_MarketTrade(Event event) {

		long startTime = System.nanoTime();



		currentMarketDay = 0;

		// run the market for a certain number of days
		while (currentMarketDay < U.days) {
			currentMarketDay++;
			U.p(" ***\n************\n*****");
			U.p(" started:   currentMarketDay:  " + currentMarketDay);

			
			//set MaxRPT for each round
			if (currentMarketDay == 1)
				U.maxRatePerTransaction = U.maxRatePerTransaction1;
			else
				U.maxRatePerTransaction = U.maxRatePerTransaction2;

			// choose agents in order to notify about trade
			for (int index = 0; index < participants.size(); index++) {

				AbstractAgent agent = (AbstractAgent) participants.get(index);
	 
				agent.eventOccurred(event);
 

			}

			// given that all agents finished their trade, now calculate market prediction for this round
			CalculateMarketPrediction();

		}
		
		// reporting purpose
		U.p(" ***\n************\n*****");
		U.p(" ended:   currentMarketDay:  " + currentMarketDay);
		long stopTime = System.nanoTime();
		System.out
				.println("time taken to finish market trade event  "
						+ (stopTime - startTime) + "  market number: "
						+ U.marketNumber);
	}

 
	
	


	
	// Reveal the Correct answer, So all the agents retrain their model, update believe and trade strategy
	public void revealResult() {
		System.out.println(" The wining value  is " + winnerValue);
		isMarketActive = false;

		
		//give first day revenue
		giveRevenuesFirstDay();

		//give other days' revenue
		for (int dayNumber = 1; dayNumber < U.days; dayNumber++) {
			if (U.giveRewardinFirstRound == false && dayNumber != U.days - 1)
				continue;
			giveRevenuesOtherDays(dayNumber);
		}
		
		
		fireEvent(new ResultRevealed(winnerValue));

 

 
	}

	
	// end market
	public void endMarket() {

		isMarketActive = false;

		
		// notify agents market is ended
		fireEvent(new MarketEndedEvent());

		
		//reset agent transactions 
		SecurityHolders = new HashMap<AbstractAgent, ArrayList<Bid>>();

		
		//reporting purpose
		Utility.WriteToReportB("\n***************\n***************\n***************\n");
		System.out.println("*****************************market Ended for DATE: "+ marketDate);

	}

	

	

	public double tradeSecurity(AbstractAgent trader,
			HashMap<Double, Double> basket) throws MyException {

		System.out.println(trader.getAgentName() + " wants to trade. \t");

		Iterator<Double> iter = basket.keySet().iterator();
		while (iter.hasNext()) {
			double pred = iter.next();
			double invest = basket.get(pred);

			System.out.println("Prediction: " + pred + "   Invest  :" + invest);

			/* set the agent prediction equal to what it bid */
			if (currentMarketDay == U.days)
				trader.getPrModel().setPredictionValue(pred);
		}
		System.out.println("");

		double cost = calTradeCost(basket);

		if (trader.getCapital() >= cost) {

			ArrayList<Bid> newHold;
			if (SecurityHolders.containsKey(trader)) {

				newHold = SecurityHolders.get(trader);

			} else {

				newHold = new ArrayList<Bid>();
			}

			String invests = "";

			Iterator<Double> iterator = basket.keySet().iterator();
			while (iterator.hasNext()) {
				double prediction = iterator.next();
				double invest = basket.get(prediction);

				invests += prediction + U.delim + invest;

				newHold.add(new Bid(prediction, invest));

			}
			SecurityHolders.put(trader, newHold);

			// updating SecurityHolders and agent information

			if (U.giveRewardinFirstRound == true) {
				trader.reduceCapital(cost);

			}

			// to commit only last day transaction only
			else if (currentMarketDay == U.days) { 
											 
				trader.reduceCapital(cost);
			}

			String output = "";

			output += U.marketNumber + U.delim + currentMarketDay + U.delim;
			;
			output += trader.getAgentName() + U.delim;

			output += invests + U.delim;
			output += trader.getCapital();

			output += "\n";

			Utility.WriteToReportT(output);

			Utility.WriteToReportB(output);

			// showHolders();

			// showStatus();
			return cost;

		} else {

			String output = "";
			output += marketDate + U.delim + currentMarketDay + U.delim;
			output += trader.getAgentName() + U.delim;
			output += "agent" + U.delim + "has" + U.delim + "no" + U.delim
					+ "no" + U.delim + " enough" + U.delim + "money" + U.delim;

			output += cost + U.delim;
			output += trader.getCapital();
			output += "\n";

			Utility.WriteToReportT(output);

			System.out
					.println("To: "
							+ trader.getAgentName()
							+ "  ->Sorry, you do not have enough money to Trade. Deal is cancelled!");

			return 0;

		}

	}

	

	
	// Option 1: for all (each predictions* its investments )/ sum of investments; 
	//option2: chooses the prediction with highest investment, ; 
	//Option3=same as option 1, but use a percentage of agents prediction, using the threshold
	public double CalculateMarketPrediction() {

		U.p("started CalculateMarketPrediction");
		// multiply each prediction by its investment and calculate their sum,
		// then divide the sum by the total of investments
		if (PredictionMethod == 1) {
			double sum = 0;
			double counter = 0;
 

		 
			Iterator<AbstractAgent> iterator = SecurityHolders.keySet()
					.iterator();
			while (iterator.hasNext()) {
				AbstractAgent agt = (AbstractAgent) iterator.next();

				ArrayList<Bid> bids = SecurityHolders.get(agt);

				double prediction = bids.get(bids.size() - 1).getPrediction();

				if (prediction != Double.NaN) {
					double invest = bids.get(bids.size() - 1).getInvest();

					counter += invest;

					sum += prediction * invest;

				}

			}
			U.p("***********");

			marketPrediction = sum / counter;

			U.p("sum:" + sum);
			U.p("counter:" + counter);

			if (Double.isNaN(marketPrediction)) {
				U.p("Market prediction is NAN");
				throw new Error("Market prediction is NAN");
				// throw new MyException("Market prediction is NAN");

			}

		} else if (PredictionMethod == 2) {

			HashMap<Double, Double> values = new HashMap<Double, Double>();

			Iterator<AbstractAgent> iterator = SecurityHolders.keySet()
					.iterator();
			while (iterator.hasNext()) {
				AbstractAgent agt = (AbstractAgent) iterator.next();

				ArrayList<Bid> bids = SecurityHolders.get(agt);

				double prediction = bids.get(bids.size() - 1).getPrediction();
				double invest = bids.get(bids.size() - 1).getInvest();

				if (values.containsKey(prediction)) {
					double sum = values.get(prediction) + invest;
					values.put(prediction, sum);
				} else
					values.put(prediction, invest);

			}

			double maxInvest = -1;
			double winPred = -1;
			Iterator<Double> iterator3 = values.keySet().iterator();
			while (iterator3.hasNext()) {
				double pred = iterator3.next();
				double invest = values.get(pred);

				if (invest > maxInvest) {
					maxInvest = invest;
					winPred = pred;
				}

			}
			marketPrediction = winPred;
		}
		// choose a percentage of agents and use their prediction and investment
		// to give final shot
		else if (PredictionMethod == 3) {

			HashMap<Double, Double> values = new HashMap<Double, Double>();
			Vector<Double> invests = new Vector<Double>();

			Iterator<AbstractAgent> iterator = SecurityHolders.keySet()
					.iterator();
			while (iterator.hasNext()) {
				AbstractAgent agt = (AbstractAgent) iterator.next();

				ArrayList<Bid> bids = SecurityHolders.get(agt);

				double prediction = bids.get(bids.size() - 1).getPrediction();
				double invest = bids.get(bids.size() - 1).getInvest();

				if (values.containsKey(prediction)) {
					double sum = values.get(prediction) + invest;
					values.put(prediction, sum);
					invests.add(sum);
				} else {
					values.put(prediction, invest);
					invests.add(invest);
				}
			}

			Collections.sort(invests, Collections.reverseOrder());
			U.p("invests");
			U.p(invests);

			{

				int thresholdIndex = (int) Math
						.ceil(U.marketIntegrationMethodTreshold
								* invests.size() / 100.0);

				U.p("U.marketIntegrationMethodTreshold: "
						+ U.marketIntegrationMethodTreshold);
				U.p("thresholdIndex: " + thresholdIndex);

				double thresholdInvest = 0;

				thresholdInvest = invests.elementAt(thresholdIndex - 1);

				U.p("thresholdInvest: " + thresholdInvest);

				double sum = 0;
				double counter = 0;

				Iterator<Double> iterator3 = values.keySet().iterator();
				while (iterator3.hasNext()) {
					double pred = iterator3.next();
					double invest = values.get(pred);

					if (invest >= thresholdInvest) {
						counter += invest;
						sum += pred * invest;
					}

					// U.p(agt.getAgentName()+"\t\t"+
					// prediction+"\t\t"+invest+"\t"+sum+"\t"+ counter);
				}
				marketPrediction = sum / counter;

				U.p("sum:" + sum);
				U.p("counter:" + counter);

				if (Double.isNaN(marketPrediction)) {
					U.p("Market prediction is NAN");
					throw new Error("Market prediction is NAN");

				}

			}

		}



		Utility.WriteToReportB("\n " + "Market number: " + U.delim
				+ U.marketNumber + U.delim + "    Market Prediction" + U.delim
				+ marketPrediction + "\n \n");// +U.delim+"Estimated Cutoff"+U.delim+estimatedCutOff+"\n \n");
		U.p("Market Prediction:" + marketPrediction);
		Utility.WriteToReportB("\n***************\n");

		if (Double.isNaN(marketPrediction))
			marketPrediction = 0;

		U.p("ended CalculateMarketPrediction");
		return marketPrediction;
	}

	
	

	public void giveRevenuesFirstDay() {

		System.out.println("-----------------------------");
		System.out.println("Agents are being given first day revenues");

 

 

		double revenue = 0;
		String rev2 = "";
		String errorStr = "";
		String investString = "";

		U.p("Correct ans: " + winnerValue);

		Vector<Double> errorList = new Vector<Double>();
		Vector<Double> prs = new Vector<Double>();
		Vector<Double> bets = new Vector<Double>();

		for (AbstractAgent agent : participants) {

			if (SecurityHolders.get(agent) != null) {

				ArrayList<Bid> bids = SecurityHolders.get(agent);

				double prediction = bids.get(0).getPrediction();
				double invest = bids.get(0).getInvest();
				double error = Math.abs(winnerValue - prediction);

				prs.add(prediction);
				bets.add(invest);
				errorList.add(error);

			}
		}

		Collections.sort(errorList);
		// finding threshold based on finding outliers
		int quartile1Index = (int) errorList.size() / 4;
		int quartile3Index = quartile1Index * 3;
		double quartile1 = errorList.get(quartile1Index);
		double quartile3 = errorList.get(quartile3Index);
		double IQR = quartile3 - quartile1;

		// if all the prediction are same, just set upperThreshold to a number bigger than their error, 
		// as it is going to be same for (as all have same error)  the way we choose this number does not matter much
		if (IQR == 0)
			IQR = 2;

		double upperThreshold = quartile3 + 1.5 * IQR;

		U.p("errorlist");
		U.p(errorList);

		if (U.giveRewardinFirstRound == true) {

			for (AbstractAgent agent : participants) {

				revenue = 0;

				double reward = 0;

				double invest = 0;

				if (SecurityHolders.get(agent) != null) {

					ArrayList<Bid> bids = SecurityHolders.get(agent);

					double prediction = bids.get(0).getPrediction();
					invest = bids.get(0).getInvest();
					double Error = Math.abs(winnerValue - prediction);

					errorStr += Error + U.delim;

					double a = (100 - 0) / (0 - upperThreshold);
					double c = 100;

					double transformedError = a * Error + c;

		

					if (transformedError <= 0)
						reward = 0;
					else
						reward = Math.log(transformedError);

					if (reward < 0)
						reward = 0;

					revenue += reward * invest;

					agent.addCapital(revenue);
					System.out.println(agent.getAgentName() + " Error: "
							+ Error + ":   reward: " + reward + " *  invest: "
							+ invest + "   = Revenue: " + revenue);

					if (Double.isNaN(agent.getCapital())) {
						throw new Error(agent.getAgentName()
								+ " capital is nan");
					}

				} else
					// add 0 revenue for all other agents
					agent.updatePerformance(0, false);

				rev2 += revenue + U.delim;
				investString += invest + U.delim;

				agent.updateMarketData(reward, revenue);

			}

			revenueString = rev2;
			errorString = errorStr;
			investmentString = investString;

		}

		previousTreshold = upperThreshold;

		U.p("end of give  first day Revenue in MarketMaker class");

		

	}

	
	// to be used to give revenue to agents for each round except first round
	public void giveRevenuesOtherDays(   int dayNumber) 
	{
		double  upperThreshold=previousTreshold;
		U.p("dayNumber:" + dayNumber);
		U.p("U.days:" + U.days);

		U.p(" \n ************\n  give  last day Revenue in MarketMaker class  \n ************\n");

		System.out.println("-----------------------------");
		System.out.println("Agents are being given their revenues");



		double revenue = 0;
	 	@SuppressWarnings("unused")
		String rev2 = "";
 

		for (AbstractAgent agent : participants) {

			revenue = 0;

			double reward = 0;

			double invest = 0;
			if (SecurityHolders.get(agent) != null) {

				ArrayList<Bid> bids = SecurityHolders.get(agent);
				
		 
				double prediction = bids.get(dayNumber).getPrediction(); 
				invest = bids.get(dayNumber).getInvest();
				double Error = Math.abs(winnerValue - prediction);
 
				
				double a = (100 - 0) / (0 - upperThreshold);
				double c = 100;
				double transformedError = a * Error + c;

 


				if (transformedError <= 0)
					reward = 0;
				else
					reward = Math.log(transformedError);

				if (reward < 0)
					reward = 0;

				revenue += reward * invest;
				
				agent.addCapital(revenue);
			 

				if (Double.isNaN(agent.getCapital())) {
					throw new Error(agent.getAgentName() + " capital is nan");
				}

			} else // add 0 revenue for all other agents
			{

				// tell agents what was their reward
				agent.updatePerformance(0, false);

			}

			rev2 += revenue + U.delim;

			agent.updateMarketData(reward, revenue);

		}

		U.p("end of give other days  Revenue in MarketMaker class");

	}

	
	

	// to scale the agents capital if they got too small or too large
	public void checkAgentsCapital() {
		if (U.autoRescaleCapital == false)
			return;

		Vector<Double> capitals = new Vector<Double>();

		for (AbstractAgent agent : participants) {

			capitals.add(agent.getCapital());
		}

		double old_min = Collections.min(capitals);
		double old_max = Collections.max(capitals);

		if (old_min == old_max) {
			for (AbstractAgent agent : participants) {

				double newCapital = 10;
				agent.setCapital(newCapital);

			}

			U.scaleCounter++;
		}

		// update Capitals
		else if (old_min < U.minCapitalThreshold
				|| old_max > U.maxCapitalThreshold) {

			U.p("Agents capital are being rescaled");

			for (AbstractAgent agent : participants) {
				double oldCapital = agent.getCapital();

				double newCapital = U.newMinCapital
						* (1 - (oldCapital - old_min)
								/ (double) (old_max - old_min))
						+ U.newMaxCapital
						* ((oldCapital - old_min) / (double) (old_max - old_min));

				agent.setCapital(newCapital);

				U.p(agent.getAgentName() + ":  " + oldCapital + "->"
						+ newCapital);

			}

			U.scaleCounter++;

		}

	}

	
	//calculate agent cost for their trade
		public double calTradeCost(HashMap<Double, Double> basket) {
			double cost = 0;
			Iterator<Double> iterator = basket.keySet().iterator();

			while (iterator.hasNext()) {
				double prediction = iterator.next();
				cost += basket.get(prediction);
			}
			return cost;
		}
		
		
	public void showStatus() {
		System.out
				.println("*****************************************************************");
		System.out
				.println(" ----------------------INFO--------------------------------------");

		printAgentInfo();

	}

	public void printAgentInfo() {
		String output = " \n \t\t Agent Info \n";

		output = output + "\n"
				+ "-----------------------------------------------------------"
				+ "\n";

		for (AbstractAgent agent : participants) {
			output = output + agent.getAgentName() + "\t \t Capital: "
					+ ((AbstractAgent) agent).getCapital() + "\n";
		}

		output = output + "\n"
				+ "-----------------------------------------------------------"
				+ "\n";

		System.out.println(output);

	}

	public ArrayList<Bid> getAgentSecurityHolding(AbstractAgent agent) {
		if (SecurityHolders.get(agent) != null)
			return SecurityHolders.get(agent);

		else {

			ArrayList<Bid> tmp = new ArrayList<Bid>();
			tmp.add(new Bid(-1.0, 0.0));
			return tmp;
		}

	}

	public void showHolders() {

		String output = " \n \t\t Security Holders Info \n";

		Iterator<AbstractAgent> iterator = SecurityHolders.keySet().iterator();

		AbstractAgent agt;

		output = output + "\t";
		output = output + "\n"
				+ "-----------------------------------------------------------"
				+ "\n";

		while (iterator.hasNext()) {

			agt = (AbstractAgent) iterator.next();
			output = output + "\n" + agt.getAgentName() + "\t   ";

			ArrayList<Bid> hold = SecurityHolders.get(agt);
			Iterator<Bid> iterator2 = hold.iterator();

			while (iterator2.hasNext()) {
				Bid bid = iterator2.next();
				double prediction = bid.getPrediction();
				double invest = bid.getInvest();

				output = output + "\n" + prediction + "\t" + invest;
			}

		}

		output = output
				+ "\n-----------------------------------------------------------"
				+ "\n";

		System.out.println(output);
	}

	
	//reporting purpose
	public boolean writeReportRandP() {
		boolean done = false;

		String outPutR = "";
		String outPutP = "";

		// adding marketDate
		outPutR += marketDate + U.delim;
		outPutP += marketDate + U.delim;

		// adding correct answer or winner security
		outPutR += winnerValue + U.delim;
		outPutP += winnerValue + U.delim;

		double MarketPrediction = marketPrediction;
		// adding Market Pediction
		outPutR += MarketPrediction + U.delim;
		outPutP += MarketPrediction + U.delim;
		outPutR += Math.abs(MarketPrediction - winnerValue) + U.delim;
		outPutR += errorString;

		String txt = "";

		// adding agent capitals after closing the market
		txt = "";
		for (AbstractAgent agent : participants) {

			txt += ((AbstractAgent) agent).getCapital() + U.delim;
		}

		outPutR += txt;

		// adding agents prediction significance separately
		txt = "";
		for (AbstractAgent agent : participants) {

			ArrayList<Bid> bids = SecurityHolders.get(agent);

			double prediction = bids.get(U.PfilePredictionIndex)
					.getPrediction();

			txt += prediction + U.delim;
		}
		outPutP += txt;

		outPutR += revenueString;

		outPutR += investmentString;

		// write to file
		outPutR += "\n";
		outPutP += "\n";
		Utility.WriteToReportR(outPutR);
		Utility.WriteToReportP(outPutP);

		done = true;

		return done;

	}

	public SimDate getMarketDate() {
		return marketDate;
	}

	public void setMarketDate(SimDate date) {
		this.marketDate = date;
	}

	public List<AbstractAgent> getParticipants() {
		return participants;
	}

	public static void setParticipants(Vector<AbstractAgent> participants) {
		MarketMaker.participants = participants;
	}

	public HashMap<AbstractAgent, ArrayList<Bid>> getSecurityHolders() {
		return SecurityHolders;
	}

	public void setSecurityHolders(
			HashMap<AbstractAgent, ArrayList<Bid>> securityHolders) {
		SecurityHolders = securityHolders;
	}

	public void setWinningValue(double winnerValue) {
		this.winnerValue = winnerValue;
	}

	public double getWinningValue() {
		return winnerValue;
	}

	public double getMarketPrediction() {
		return marketPrediction;
	}

	public void setMarketPrediction(double marketPrediction) {
		this.marketPrediction = marketPrediction;
	}

	public void newExperiment() {
		fireEvent(new newExperimentStartedEvent());
		System.out.println(" New Experiment started ");
	}
	
	



	public SimDate getToday() {
		return today;
	}

	public void setToday(SimDate today) {
		this.today = today;
	}

	public void addAgent(AbstractAgent agent, int position) {
		participants.add(position, agent);

	}

	public void removeAgent(AbstractAgent agent) {
		participants.remove(agent);

	}

	public Vector<AbstractAgent> removeAgent(Set<Integer> deletion) {
		Vector<AbstractAgent> deletedAgents = new Vector<AbstractAgent>();

		Iterator<Integer> iterator = deletion.iterator();
		while (iterator.hasNext()) {
			int index = iterator.next();
			deletedAgents.add(participants.get(index));

		}

		for (int i = 0; i < deletedAgents.size(); i++) {
			participants.remove(deletedAgents.elementAt(i));
		}
		return deletedAgents;
	}

	public Vector<AbstractAgent> removeAgentContainName(String agentName) {

		Vector<AbstractAgent> deletion = new Vector<AbstractAgent>();

		System.out
				.println("# of agents before deletion:" + participants.size());

		for (AbstractAgent agent : participants) {
			if (agent.getAgentName().contains(agentName)) {

				deletion.add(agent);

			}
		}

		for (AbstractAgent agent : deletion) {

			participants.remove(agent);

			System.out.println(agent.getAgentName() + " is deleted.");

		}

		System.out.println("# of agents after deletion:" + participants.size());

		return deletion;
	}

	public void removeAllAgents() {
		participants = new Vector<AbstractAgent>();

	}

	public void fireEvent(Event event) {
		for (AbstractAgent agent : participants) {
			agent.eventOccurred(event);

		}
	}


}
