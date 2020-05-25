package tradeStrategy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Vector;

import market.MarketMaker;
import Utill.U;
import Utill.Utility;
import agents.RegressionAgent;

public class QlearningRegTradeStaretgy extends TradeStrategy_Regression {

	protected static MarketMaker mm;

	int action = 1;
	int actionSize = U.numberOfActions;

	// the amount to change the  prediction toward market  prediction;
	BigDecimal alpha = new BigDecimal(0.01, U.mc); 
	
	/*****set the Q-table****/

	
	
	// number of features to recognize states. 
	//two: one for market prediction different and  one for round number
	int featureSize = 2; 
	
	// number of feature groups (state groups), how the prediction differences to be categorized ( e.g low, medium, high)
	// Using the online version of k-means clustering [MacQueen et al., 1967],
	int numberOfClusters = U.numberOfClusters; 

	// number of columns: one for the proposed alphaRate
	int numberOfColumns = featureSize + 1 + actionSize; 

	//number of rows: number of rounds * number of clusters													 
	int numberOfRows = U.days * numberOfClusters;


	BigDecimal Q[][] = new BigDecimal[numberOfRows][numberOfColumns];

	
	// Q-table example
	// |day| Difference | alpha | action1 |action 2-|
	// |1--|small-------| alpha |Q---------|Q--------|
	// |1--|medium------| alpha |Q---------|Q--------|
	// |1--|High--------| alpha |Q---------|Q--------|
	// |2--|small-------| alpha |Q---------|Q--------|
	// |2--|medium------| alpha |Q---------|Q--------|
	// |2--|High--------| alpha |Q---------|Q--------|

	int dayIndex = 0;
	int diffIndex = 1;
	int alphaIndex = 2;
	int action1Index = 3;

	BufferedWriter outRL;
	
	// the threshold error which results in 0 revenue
	double upperThreshold;

	String agentName;
	int agentNumber;

	// constructor
	public QlearningRegTradeStaretgy(MarketMaker mm, String agentName,
			int agentNumber) {
		super(mm);

		this.agentName = agentName;
		this.agentNumber = agentNumber;
		QlearningRegTradeStaretgy.mm = mm;

		// Initialize Q-table to zero
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++)
				Q[i][j] = new BigDecimal(0, U.mc);
		}

		double counter = 1;

		// Initialize Q-table
		BigDecimal diff = new BigDecimal(0.001, U.mc);
		double round = 1;
		for (int i = 0; i < numberOfRows; i++) {

			Q[i][dayIndex] = new BigDecimal(round, U.mc);
			Q[i][diffIndex] = diff;

			if (counter < numberOfClusters) {
				counter = counter + 1;
				diff = diff.add(new BigDecimal(0.001, U.mc));
			}

			else {
				counter = 1;
				round++;
				diff = new BigDecimal(0.001, U.mc);

			}

		}

		// for reporting purpose
		if (U.writeRlFileForEachAgent) {
			File fileoutRL = new File("Results/Details/" + Utility.timeStamp
					+ "-" + agentName + "-outRL-" + ".txt");
			FileWriter fstreamoutRL;
			try {
				fstreamoutRL = new FileWriter(fileoutRL);

				outRL = new BufferedWriter(fstreamoutRL);
				String output = "\n";
				outRL.write(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		String output = "Just initiliased Q table";

		// for reporting purpose
		WriteToReportRL(output);
		printQtable();

	}

	
	//Decide what to predict and how much to bid in its prediction
	public HashMap<Double, Double> giveBasket(RegressionAgent agent) {
		String output = " we are in giveBasket  for  Market number: "
				+ U.marketNumber + " round= " + mm.currentMarketDay;
		output += "\n********************** \n ********************** ********************** \n ********************** ********************** \n ********************** ********************** ";
		WriteToReportRL(output);

		HashMap<Double, Double> basket = new HashMap<Double, Double>();
		double bid = 0;
		double currentPr = agent.getCurrentPrediction();
		double capital = agent.getCapital();
		// double confidence = agent.getConfidence();
		double maxMoney = U.maxRatePerTransaction * capital;

		upperThreshold = mm.previousTreshold;

		if (U.marketNumber == 1) {
			bid = U.minRatePerTransaction * capital;
			basket.put(currentPr, bid);

			// for reporting purpose
			output = "my prediction: " + currentPr;
			output += "first market => maxmoney";
			WriteToReportRL(output);
			return basket;

		} else if (mm.currentMarketDay == 1) {
			U.p("First Round---- " + agent.getAgentName());

			if (U.useConfidence == false)
				bid = maxMoney;

			else {

				bid = Math.min(agent.getConfidence() * agent.getCapital(),maxMoney);
				bid = Math.max(bid, U.minRatePerTransaction * capital);

			}

			basket.put(currentPr, bid);
			
			// for reporting purpose
			U.p("cofidence: " + agent.getConfidence());
			U.p("maxMoney: " + maxMoney + "   minMoney: "
					+ U.minRatePerTransaction * capital);
			U.p("bid: " + bid);

			output = "my prediction: " + currentPr;
			output += "first market => \n  bid=maxMoney => bid= " + bid;
			WriteToReportRL(output);
			
			
			
			return basket;

		}

 
		// for reporting purpose
		output = "my initial prediction= " + currentPr + " \n";
		output += "market prediction: " + mm.getMarketPrediction() + "\n";

		
		// calculate estimated error
		double estimatedError = mm.getMarketPrediction()- agent.getCurrentPrediction(); 
		BigDecimal diff = new BigDecimal(estimatedError, U.mc);
		
		//recognize state
		int stateIndex = findState(diff, mm.currentMarketDay);
		
		// select the action with the highest Q-value based on the state
		action = argmax(stateIndex);
		// alpha=Q[stateIndex][alphaColumn];

		
		// for reporting purpose
		output += "estimatedError:  " + estimatedError + "\n";
		output += "stateIndex:  " + stateIndex + "\n";
		output += "best action:  " + action + "\n";
		output += "alpha for this state:  " + alpha + "\n";
		output += "Capital: " + capital + "  maxMoney: " + maxMoney + "\n";
		WriteToReportRL(output);

		int index = agentNumber - U.firstColumnNumber;

		// for reporting purpose
		U.p("agent........ number: " + agentNumber + " index=  " + index
				+ this.toString());
		U.p("action: " + action);

		U.actionStatistic[index][action - 1] = U.actionStatistic[index][action - 1] + 1;
		U.actionStatistic2[U.marketNumber - 1][index] = action;

		
		//execute selected action
		switch (action) {

		
		//if the first action is selected
		case 1:
			//decide the amount to invest
			bid = giveBidStrategyOne(estimatedError, capital, maxMoney);
			break;

	    //if the second action is selected
		case 2:
			// remember the best alpha for this state 
			alpha = Q[stateIndex][alphaIndex];
			
			//  update the prediction by moveing toward market prediction by alpha rate
			currentPr = currentPr
					+ (alpha.multiply(new BigDecimal(estimatedError, U.mc)))
							.doubleValue();

			//calculate the new estimated error
			estimatedError = Math.abs(currentPr - mm.getMarketPrediction());
			
			//decide the amount to invest
			bid = giveBidStrategyOne(estimatedError, capital, maxMoney);
			
			// for reporting purpose
			output = " Predction after multiply alpha: " + currentPr
					+ "\n  new estimatedError: " + estimatedError + "\n";
			WriteToReportRL(output);

		
			break;

		}

		//if bid is more than MaxRpt*budget then reduce
		if (bid > maxMoney)
			bid = maxMoney;

		//add prediction and investment to the basket
		basket.put(currentPr, bid);

		// for reporting purpose
		output = "action = " + action + "  bid = " + bid + "  pr = "
				+ currentPr;
		WriteToReportRL(output);
		
		
		return basket;
	}

	


	// Update the believe once the true outcome is revealed 
	public void updateBelief(double previousReward,
			BigDecimal[] marketPrDifference, double[] myPrediction,
			double[] myCapital, double[] myConfidence, double correctAns,
			BigDecimal[] marketPr) {

		String output = " \n we are in update beleif \n  :::::::::::::::: \n";
		WriteToReportRL(output);

 
		upperThreshold = mm.previousTreshold;

		// for reporting purpose
		WriteToReportRL("previousReward:" + previousReward);
		WriteToReportRL("correctAns:" + correctAns);

		WriteToReportRL("myPrediction");
		WriteToReportRL(U.convertToString(myPrediction));

		WriteToReportRL("MarketPrediction");
		WriteToReportRL(U.convertToString(marketPr));

		WriteToReportRL("marketPrDifference");
		WriteToReportRL(U.convertToString(marketPrDifference));

		WriteToReportRL("myCapital");
		WriteToReportRL(U.convertToString(myCapital));

		/*
		 * 1- see where the the state belongs to, if necessary change the sate
		 * compositions 
		 * 2- propose alpha value for the state 
		 * 3-Calculate Q-value for each action in the current state based on the old alpha
		 *  4-propose new alpha for each state
		 */
		output = "";
		for (int day = 2; day <= U.days; day++) {
			BigDecimal diff = marketPrDifference[day - 1];

			//  see where the the state belongs to, if necessary change the sate compositions 
			updateStates(diff, day);
			int stateIndex = findState(diff, day);
			
			// for reporting purpose
			output += "correctAns: " + correctAns + " \n ";
			output += "Round: " + day + "  stateIndex= " + stateIndex + " \n ";

			double myPr = myPrediction[day - 1];
			double capital = myCapital[day - 1];
			// double confidence = myConfidence[day - 1];
			double maxMoney = capital * U.maxRatePerTransaction;
			double error = Math.abs(myPr - correctAns);
 
			// for reporting purpose
			output += "myPr: " + myPr + " \t error: " + error + "\n";
			WriteToReportRL(output);

			// propose alpha value for the state 
			if (diff.doubleValue() == 0)
				alpha = new BigDecimal(0, MathContext.DECIMAL32);
			else {

				double realError = correctAns - myPr;
				double estError = marketPr[day - 1].doubleValue() - myPr;

				//calculate alpha
				alpha = new BigDecimal(realError / estError,
						MathContext.DECIMAL32);
				WriteToReportRL("alpha = " + alpha);
			}
			//if alpha is bigger than one, set it to 1
			if (alpha.compareTo(new BigDecimal(1, MathContext.DECIMAL32)) == 1) {
				alpha = new BigDecimal(1, MathContext.DECIMAL32);
				WriteToReportRL("alpha is bigger than one => alpha: " + alpha);
			}
			//if alpha is less than 0, set it to 0
			if (alpha.compareTo(new BigDecimal(0, MathContext.DECIMAL32)) == -1) {
				alpha = new BigDecimal(0, MathContext.DECIMAL32);
				WriteToReportRL("alpha is bigger than one => alpha: " + alpha);
			}
			//store the new alpha in the Q-table
			Q[stateIndex][alphaIndex] = alpha;


			// for reporting purpose
			WriteToReportRL("alpha for state index: " + stateIndex + "now is :"
					+ alpha);

			
			
			double bid = 0;
			double estimatedPr = myPr;
			
			//calculate how the agent participated (traded) if had executed different actions
			for (int j = action1Index; j < numberOfColumns; j++) {

				int actionIndex = j - action1Index + 1;
				
				// for reporting purpose
				output = " \n action:" + actionIndex + " \n************\n";
				WriteToReportRL(output);
				output = "";

				switch (actionIndex) {

				//if had executed the first action
				case 1:

					bid = giveBidStrategyOne(error, capital, maxMoney);
					
					// for reporting purpose
					output += " \n correctAns: " + correctAns;
					output += " \n   error: " + error;
					
					break;

					//if had executed the second action
				case 2:

					estimatedPr = myPr + (alpha.multiply(diff)).doubleValue();
					error = Math.abs(estimatedPr - correctAns);
					bid = giveBidStrategyOne(error, capital, maxMoney);
					
					// for reporting purpose
					output += " \n  Alpha: " + alpha
							+ "  \n my pr and market pr diff: " + diff
							+ "\n my old pr:" + myPr
							+ " pr after multiply br alpha:" + estimatedPr;
					output += " \n correctAns: " + correctAns;
					output += " \n new error: " + error;

					break;

				}

				if (bid > maxMoney)
					bid = maxMoney;

				//what would have been its reward
				double expectedReward = calculateExpectedReward(error);
				double revenue = expectedReward;
				
				// for reporting purpose
				output += " \n expectedReward: " + expectedReward + "\n";
				output += " \n revenue: " + revenue + "\n";

		 
				//update Q-value for each action based on its reward
				double newQ = (1 - U.Qalpha) * Q[stateIndex][j].doubleValue()
						+ (U.Qalpha) * revenue;
				Q[stateIndex][j] = new BigDecimal(newQ, U.mc);
				
				// for reporting purpose
				output += " expectedReward =" + expectedReward + " revenue: "
						+ revenue;
				
				WriteToReportRL(output);
			}

		}

		printQtable();
	}
	
	// once a market is over, agents update the boundary of state clusters 
	public void updateStates(BigDecimal diff, int round) {
		diff = diff.abs();

		String output = "";

		// for reporting purpose
		WriteToReportRL("in update satets...    diff=" + diff + "    round="
				+ round);

		int startIndex = (round - 1) * numberOfClusters;
		int endIndex = startIndex + numberOfClusters;

		int index = startIndex;

		BigDecimal minDistance = (Q[index][diffIndex].subtract(diff)).abs();
		
		// find the closer header to the diff
		for (int i = startIndex + 1; i < endIndex; i++) {

			BigDecimal distance = (Q[i][diffIndex].subtract(diff)).abs();
			if ((distance.compareTo(minDistance) == -1)) // less than
			{
				index = i;
				minDistance = distance;
			}
		}
		output += " closest header : " + index + " \n";

		printQtable();

		// find the closest header to the selected header
		BigDecimal header = Q[index][diffIndex];
		BigDecimal headerMinDistance = new BigDecimal(1000000, U.mc);
		int indexHeaderMinDistance = -1;
		for (int i = startIndex + 1; i < endIndex; i++) {
			if (i != index) {
				BigDecimal distance = (header.subtract(Q[i][diffIndex])).abs();
				if (distance.compareTo(headerMinDistance) == -1) {
					indexHeaderMinDistance = i;
					headerMinDistance = distance;
				}
			}

		}
		output += " closest header to the selected header: "
				+ indexHeaderMinDistance + " \n";
		// if the difference of the minDistance is smaller than the difference
		// of the header and the nearest cluster
		// then merge those two headers and use the average
		if (headerMinDistance.compareTo(minDistance) == -1) {
			Q[indexHeaderMinDistance][diffIndex] = (Q[indexHeaderMinDistance][diffIndex]
					.add(Q[index][diffIndex])).divide(new BigDecimal(2, U.mc));
			for (int i = alphaIndex; i < numberOfColumns; i++)
				Q[indexHeaderMinDistance][i] = (Q[indexHeaderMinDistance][i]
						.add(Q[index][i])).divide(new BigDecimal(2, U.mc));

			Q[index][diffIndex] = diff;

			output += "index: " + indexHeaderMinDistance + " and index: "
					+ index + " are merged. \n";
		} else {

			output += "index: " + index + " has new value. " + diff + " + "
					+ Q[index][diffIndex] + " /2 = ";
			
			Q[index][diffIndex] = (diff.add(Q[index][diffIndex]))
					.divide(new BigDecimal(2, U.mc));
		}

		WriteToReportRL(output);

		printQtable();

	}


	
	
	
	// calculate expected reward based on agent error
	double calculateExpectedReward(double Error) {
	 

		Error = Math.abs(Error);
		double a = (100 - 0) / (0 - upperThreshold);
		double c = 100;
		double transformedError = a * Error + c;

		double expectedReward=0;

		if (transformedError <= 0)
			expectedReward = 0;
		else
			expectedReward = Math.log(transformedError);

		if (expectedReward < 0)
			expectedReward = 0;

		return expectedReward;
	}

	
	// deciding investment- strategy 1
	double giveBidStrategyOne(double error, double capital, double maxMoney) {

		double bid = 0;

		double expectedReward = calculateExpectedReward(error);

		if (expectedReward > 1) {
			bid = capital * U.maxRatePerTransaction;

		} else
			bid = capital * U.minRatePerTransaction;

	 

		return bid;
	}

	
	// deciding investment- strategy 2
	double giveBidStrategyTwo(double error, double capital, double maxMoney) {

		double bid = 0;
		double expectedReward = calculateExpectedReward(error);

		bid = (maxMoney - U.minRatePerTransaction * capital) / Math.log(100)
				* expectedReward + (U.minRatePerTransaction * capital);

		String output = "error: " + error + " expectedReward: "
				+ expectedReward + " maxMoney" + maxMoney;
		WriteToReportRL(output);

		return bid;
	}

	
	
	// deciding investment- strategy 3
	double giveBidStrategyThree(double error, double capital, double maxMoney) {
		double bid = 0;

		double expectedReward = calculateExpectedReward(error);

		if (expectedReward > 1)

			bid = maxMoney * expectedReward / Math.log(100);

		else
			bid = capital * U.minRatePerTransaction;

		String output = "error: " + error + " expectedReward: "
				+ expectedReward + " maxMoney" + maxMoney;
		WriteToReportRL(output);
		return bid;
	}
	
	
	
	
	// finds the state based on agent prediction difference with market prediction
	public int findState(BigDecimal diff, int round) {
		diff = diff.abs();
		

		// for reporting purpose
		WriteToReportRL("we are in findState.   " + "\n market Pr:"
				+ mm.getMarketPrediction() + "\n diff=" + diff
				+ "  \n     round:" + round);
		printQtable();

		int startIndex = (round - 1) * numberOfClusters;
		int endIndex = startIndex + numberOfClusters;

		int index = startIndex;
		BigDecimal minDistance = (Q[index][diffIndex].subtract(diff)).abs();

		for (int i = startIndex + 1; i < endIndex; i++) {
			BigDecimal dinstance = (Q[i][diffIndex].subtract(diff)).abs();
			if (dinstance.compareTo(minDistance) == -1) {
				index = i;
				minDistance = dinstance;
			}
		}

		alpha = Q[index][alphaIndex];


		// for reporting purpose
		String output = "";
		output = output + "    closest diff=    " + Q[index][diffIndex];
		output += " =>      index=" + index + "     " + "alpha=    " + alpha;
		WriteToReportRL(output);

		return index;
	}

	// choose an action with highest Q, if more than one exists, choose the most frequent one
	public int argmax(int stateIndex) {
		String output = " we are in argmax...  ";
		WriteToReportRL(output);
		/* 1- go through the Q array and find the maximum Q, return the index */
		int firstActionIndex = action1Index;
		int maxActionIndex = firstActionIndex;
		// int minActionIndex = firstActionIndex;

		
		
		// first column= state, second column= state, third column=alpha,
		BigDecimal maxQ = Q[stateIndex][firstActionIndex]; 
															

		for (int j = firstActionIndex + 1; j < numberOfColumns; j++) {
			if (Q[stateIndex][j].compareTo(maxQ) == 1) // if bigger
			{
				maxQ = Q[stateIndex][j];
				maxActionIndex = j;

			}

		}

		maxActionIndex = maxActionIndex - firstActionIndex;

		// find all actions with Q value equal to maxQ
		Vector<Integer> equalIndex = new Vector<Integer>();

		for (int j = firstActionIndex; j < numberOfColumns; j++) {
			output = " \n  action:  " + (j - firstActionIndex) + " value: "
					+ Q[stateIndex][j];
			if (Q[stateIndex][j].compareTo(maxQ) == 0) // if equal
			{
				equalIndex.add(j);
				output += " \n  action:  " + (j - firstActionIndex)
						+ "  is equal to:" + maxQ + " \n";
			}

		}

		output += " \n  action index with equal Q values: \n";
		for (int i = 0; i < equalIndex.size(); i++)
			output += (equalIndex.elementAt(i) - firstActionIndex) + " ,  ";

		WriteToReportRL(output);

		// if more than one action with the highest Q value exist
		if (equalIndex.size() > 1) {

			U.p("agentNumber" + (agentNumber - U.firstColumnNumber));
			WriteToReportRL("agentNumber:"
					+ (agentNumber - U.firstColumnNumber));

			Vector<Integer> actionFrVec = U.getActionFrequency(agentNumber
					- U.firstColumnNumber);
			U.p("actionFrVec " + actionFrVec);
			WriteToReportRL("actionFrVec " + U.convertToString(actionFrVec));

			// choose the most frequent action , if more than one has the
			// highest Q value
			int maxCount = 0;
			int MaxActionNo = 0;
			for (int i = 0; i < equalIndex.size(); i++) {
				int candidateAction = equalIndex.elementAt(i) - firstActionIndex;
				int candidateActionCount = actionFrVec.elementAt(candidateAction);

				// for reporting purpose
				WriteToReportRL("candidateAction= " + candidateAction);
				WriteToReportRL("candidateActionCount= " + candidateActionCount);

				if (candidateActionCount >= maxCount) {
					maxCount = candidateActionCount;
					MaxActionNo = i;

				}
			}

			maxActionIndex = MaxActionNo;

			// for reporting purpose
			output += " \n most frequent action ( " + maxActionIndex
					+ ") is chosen out of  :" + equalIndex.size()
					+ "  number of action with same Q value";
			WriteToReportRL("\n most frequent action ( " + maxActionIndex
					+ ") is chosen out of  :" + equalIndex.size()
					+ "  number of action with same Q value");
			output += " \n =>   best action: " + (maxActionIndex + 1);
			output += " \n =>   best action: " + (maxActionIndex + 1);
			WriteToReportRL(output);
		}

		return (maxActionIndex + 1);
	}

    // for reporting purpose
	public void printQtable() {
		String output = "Q table \n -------------- \n \t Days \t round diff \t  alpha  \t a1 \t  a2 ";
		for (int i = 0; i < numberOfRows; i++) {
			output = output + " \n ";
			for (int j = 0; j < numberOfColumns; j++)
				output = output + " \t " + Q[i][j];
		}
		output = output + " \n ";

		WriteToReportRL(output);
	}

    // for reporting purpose
	public void WriteToReportRL(String str) {

		if (U.writeRlFileForEachAgent) {
			try {

				outRL.write(str + "\n \n");

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

}
