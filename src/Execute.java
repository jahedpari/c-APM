
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import market.MarketMaker;
import Data.DataDistributer;
import Utill.MyException;
import Utill.SimDate;
import Utill.U;
import Utill.Utility;
import agents.AbstractAgent;

public class Execute {

	static MarketMaker mm;
	static Vector<AbstractAgent> agentList = new Vector<AbstractAgent>();
	static String fileID;

	public static void Run(MarketMaker m) {

		mm = m;

		// 0 for Q-learning, 1 for constant
		int tradeStrategyNumber = 0; 
		
		// set the trading strategy
		U.tradeStrategyName = U.tradeStrategyNameVec[tradeStrategyNumber];

		
	 // to read each data set (for different application domains)
		/**/for( int df=0; df<U.dataFileArr_UCI.length ; df++)
		{

			 
			 /**/String dataFile= U.dataFileArr_UCI[df]; 			/* UCI */
			   //dataFile = U.dataFileArr_UCI[0];

			
			// For reporting purpose only
			U.fileInfo = dataFile.replaceAll("UCIFinalPredictionWithR/", "")
					.replaceAll("UCIFinalR-normalized/", "")
					.replaceAll(".txt", "").replace("Data/", "");
			U.fileInfo = U.fileInfo.replaceAll("UCIDataSet/", "")
					.replaceAll("Data/", "").replace("/", "");
			U.p("----------------------\n ----------------\n----------------\n------------");
			U.p("nex experiments for file: " + dataFile);
			U.p("----------------------\n ----------------\n----------------\n------------");
			U.setlastDataColumnNumber(dataFile);

			U.p("number of records= " + U.numberofRecords);
			U.p("limit number= " + U.limit);

			String fileInfo = " datafile:  "
					+ dataFile.replaceAll("Data/UCIFinalPredictionWithR/", "")
							.replaceAll(".txt", "");
			fileInfo = fileInfo.replaceAll("Data/UCIDataSet/", "").replaceAll(
					"Data/", "");

			U.ExperimentNote = fileInfo;

			
			// to run number of times
			for (int r = 0; r < U.NumberOfRuns; r++) {

				// to experiment for different days (i.e number of rounds)
				for (int days = U.minDay; days <= U.maxDay; days += U.daySteps) {
					U.days = days;

					
					// to reset
					U.globalSetting(); 
					agentList.removeAllElements();
	
					// create agents
					Vector<AbstractAgent> tempAgentList = AgentCreation.createAgents(mm);

					for (int i = 0; i < tempAgentList.size(); i++) {

						agentList.add(tempAgentList.elementAt(i));

					}

					// Registering all the agents to the market and allocate them equal capital
					mm.removeAllAgents();
					for (int a = 0; a < agentList.size(); a++) {
						agentList.elementAt(a).setCapital(U.money);
						mm.addAgent(agentList.elementAt(a), a);

					}

					 
					// Assign each data source to each agent
					DataDistributer myDataDistributer = new DataDistributer(
							dataFile, mm);

					int min = U.firstColumnNumber;
					int max = U.lastColumnNumber;

					String outMA = "";

					// give them column which their names points to (e.g a6 reads column 6 of the data set)
					if (U.giveAgentsData_perTheirName) {
						// telling what column each agent has access to
						for (int a = 0; a < agentList.size(); a++) {

							int[] colsIndex = new int[U.maxSizeforAgentDataAccess];

							for (int i = 0; i < U.maxSizeforAgentDataAccess; i++) {
								colsIndex[i] = -1;
							}

							String intValue = agentList.elementAt(a)
									.getAgentName().replaceAll("[^0-9]", "");
							int col = Integer.parseInt(intValue);
							U.p(agentList.elementAt(a).getAgentName() + "   "
									+ col + " ");
							colsIndex[0] = col;
							outMA += agentList.elementAt(a).getAgentName()
									+ U.delim + col + "\n";

							myDataDistributer.updateagentDataAccessIndex(a,
									colsIndex);
						}

					}
					// randomly give different number of attributes to each agent
					else if (U.giveAgentsData_multiAtt_byChance) {
						U.numberOfClassifiers = agentList.size()
								/ (U.lastColumnNumber - U.firstColumnNumber);
						Set<Integer> set = new HashSet<Integer>();
						int counter = -1;
						// telling what column each agent has access to
						for (int a = 0; a < agentList.size(); a++) {

							outMA += agentList.elementAt(a).getAgentName()
									+ U.delim;

							// generate new st of random number ( choose
							// columns)
							// after giving same data to all classifier
							counter++;

							if (counter % U.numberOfClassifiers == 0) {

								// fill the set with random numbers
								set.clear();
								while (set.size() < U.maxSizeforAgentDataAccess) {
									int col = min
											+ U.randomGenerator
													.nextInt((max - min));
									set.add(col);

									// U.p("col= "+ col);
								}
							}

							// copy the set to the array
							int[] colsIndex = new int[U.maxSizeforAgentDataAccess];
							int index = 0;
							for (Integer value : set) {

								colsIndex[index] = value;

								outMA += colsIndex[index] + U.delim;
								index++;
							}

							outMA += "\n";

							myDataDistributer.updateagentDataAccessIndex(a,
									colsIndex);

						}
					}
					// give all attributes to each agent
					else if (U.giveAgentsData_multiAtt_allData == true) {

						// telling what column each agent has access to
						for (int a = 0; a < agentList.size(); a++) {

							int[] colsIndex = new int[U.maxSizeforAgentDataAccess];
							U.p("U.numberOfAgents:" + U.numberOfDataColumns);

							for (int i = min; i < max; i++) {
								colsIndex[i - min] = i;

							}

							U.p(" agent " + a + " has these cols: \n");
							String out = "";
							for (int i = 0; i < colsIndex.length; i++) {
								out += colsIndex[i] + "  ";
							}
							U.p(out + "\n");

							outMA += agentList.elementAt(a).getAgentName()
									+ U.delim + "all cols" + "\n";
							myDataDistributer.updateagentDataAccessIndex(a,
									colsIndex);
						}

					}

					// ***************************************************
					// ***********RUN EXPERIMENT***************************
					// ***************************************************

					runExperiment(myDataDistributer, outMA);

					System.out
							.println(" number of times capitals are rescaled:  "
									+ U.scaleCounter);

				}
			}

		}

	}

	// Run Experiment
	public static int runExperiment(DataDistributer myDataDistributer,String outMA) {

		mm.printAgentInfo();

		// to notify new experiment is about to start
		mm.newExperiment();

		// reporting purpose
		Utility.openFiles(mm);
		Utility.WriteToReportQ("---------------- " + U.delim + "New RUN"
				+ U.delim + " ------------" + "\n");
		Utility.WriteToReportMA(outMA);

		long startTime = System.nanoTime();
		Utility.WriteToReportTime("setup done " + U.delim + startTime);



		int marketNumber = 0;

		Vector<SimDate> trainingExamples = myDataDistributer.giveMonitorDates();

		// to store correct answer and market prediction into history to calculate MAE, MSE
		Vector<Double> correctAns = new Vector<Double>();
		Vector<Double> marketPrediction = new Vector<Double>();

		int tmpCounter = 0;
		
		// for each record in the data set (each record is specified by a date which should be in the first column of the data set)
		for (SimDate date : trainingExamples) {
			long startTimeRecord = System.nanoTime();

			tmpCounter++;

			// ignore first X records
			if (tmpCounter <= U.ignoreFirstXMarket)
				continue;

			// stop if we reach the limit
			if (marketNumber >= U.limit)
				break;

			marketNumber++;
			U.marketNumber = marketNumber;

			Utility.WriteToReportTime2(marketNumber + U.delim);

			System.out.println(" market number: " + marketNumber);

			// specify the correct answer for this record
			double winningValue = myDataDistributer
					.giveCorrectValue(marketNumber + U.ignoreFirstXMarket);

			// set market correct answer
			mm.setWinningValue(winningValue);

			// give data to agents for this market (record)
			U.p("Data distributer is giving data to agents..");
			myDataDistributer.giveDataToAgents(marketNumber
					+ U.ignoreFirstXMarket);

			// to scale the agents capital if they got too small or too large
			mm.checkAgentsCapital();
			
			// start market
			mm.startMarket();

			// reveal the result to all the agents 
			mm.revealResult();

			// store correct answer and market prediction into history
			correctAns.add(winningValue);
			marketPrediction.add(mm.getMarketPrediction());

			// reporting purpose
			mm.writeReportRandP();
			
			// end market
			mm.endMarket();
			
			// reporting purpose
			Utility.WriteToReportTime2("\n");
			long stopTimeRecord = System.nanoTime();
			double timeSpent = (stopTimeRecord - startTimeRecord);
			Utility.WriteToReportTime("market " + marketNumber + " done "
					+ U.delim + timeSpent);
		}

		// reporting purpose
		writeToReports(mm, marketNumber, correctAns, marketPrediction);
		Utility.WriteActionStatistic();
		Utility.WriteActionStatistic2();

		Utility.closeFiles();
		return marketNumber;
	}

	// reporting purpose
	@SuppressWarnings("unused")
	public static void writeToReports(MarketMaker mm, int numberOfRecords,
			Vector<Double> correctAns, Vector<Double> marketPrediction) {
		fileID = Utility.timeStamp;

		double MAE = calculateMeanAbsoluteError(correctAns, marketPrediction);

		double MSE = calculateMeanSquaredError(correctAns, marketPrediction);
		Utility.WriteToReportR("number of Records: " + numberOfRecords + "\n");

		Utility.WriteToReportR("\n Mean Absolute Error: " + MAE + "\n");

		// Adding experiment info
		Utility.WriteToReportR("Participants" + U.delim + "Capital" + U.delim
				+ " Confidence" + U.delim + " TradeStrategy" + U.delim
				+ " Pridiction Model\n");
		Utility.WriteToReportR("\n-----------------------------------------------------------------------------------------------------\n");

		String agentName = "";
		String capital = "";

		String AgentInfo = "";
		for (AbstractAgent agt : mm.getParticipants()) {
			Utility.WriteToReportR(agt.getAgentName() + U.delim
					+ agt.getCapital() + U.delim + agt.getConfidence()
					+ U.delim);
			Utility.WriteToReportR(agt.getTradeStartegy().getClass()
					.getSimpleName()
					+ U.delim
					+ agt.getPrModel().getClass().getSimpleName()
					+ "\n");
			agentName += agt.getAgentName() + "-";
			capital += (double) agt.getCapital() + "-";

		}

		Utility.WriteToReportR("Number of Days (number of allowed transactions for each market for each agent): "
				+ U.days + "\n");

		Utility.WriteToReportR(" Experimented on:" + fileID + "\n");

		// ****** Writing to summary file

		String output = "\n";

		output += U.ExperimentNote + U.delim + fileID + U.delim
				+ mm.getParticipants().size() + U.delim;
		output += U.tradeStrategyName + U.delim;
		output += U.days + U.delim;
		output += numberOfRecords + U.delim;
		output += U.maxRatePerTransaction1 + U.delim;
		output += U.maxRatePerTransaction2 + U.delim;
		output += U.minRatePerTransaction + U.delim;

		output += MAE + U.delim;

		output += MSE + U.delim;

		output += AgentInfo;

		Utility.WriteToReportSummary(output, mm);

		U.p("Number of Agents: " + mm.getParticipants().size());
		U.p("Number of records: " + numberOfRecords);
		U.p("Mean Absolute Error: " + MAE);
	}

	public static double calculateMeanSquaredError(Vector<Double> x,
			Vector<Double> y) {
		double sum = 0;

		if (x.size() != y.size())
			try {

				throw new MyException("size are not same. x size= " + x.size()
						+ " y size=" + y.size() + " \n");
			} catch (MyException e) {

				e.printStackTrace();
			}

		for (int i = 0; i < x.size(); i++) {
			sum += Math.pow((x.elementAt(i) - y.elementAt(i)), 2);
		}

		if (x.size() != 0)
			return sum / (double) x.size();
		else
			return 0;
	}

	public static double calculateMeanAbsoluteError(Vector<Double> x,
			Vector<Double> y) {
		double sum = 0;

		if (x.size() != y.size())
			try {
				throw new MyException("size are not same");
			} catch (MyException e) {

				e.printStackTrace();
			}

		for (int i = 0; i < x.size(); i++) {
			sum += Math.abs(x.elementAt(i) - y.elementAt(i));
		}

		if (x.size() != 0)
			return sum / (double) x.size();
		else
			return 0;
	}

}



//**/ for( df=0; df<U.dataFileArrSm.length; df++)
		// **/ for( df=0; df<U.dataFileArr_ALL.length; df++)
		// for( df=0; df<U.dataFileArr_GFT.length; df++)
		//**/for( df=0; df<U.dataFileArr_UCI.length ; df++)
	//	{

			/* four network types */
			// **/String dataFile= U.dataFileArrSm[df]; /* for scenario1-4 */
			// String dataFile= U.dataFile0; /* for scenario5-6 */
			// String dataFile= U.dataFile112; /* for scenario7 */
			// **/String dataFile= U.dataFileArr_ALL[df]; /* all data */
			// **/String dataFile= U.dataFileArr_GFT[df]; 	/* for GFT */
			 //**/String dataFile= U.dataFileArr_UCI[df]; 			/* UCI */
			 