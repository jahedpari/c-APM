package Utill;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Vector;

import Data.Data;
import agents.AbstractAgent;
import market.MarketMaker;

public class Utility {

	static BufferedWriter outR;
	static BufferedWriter outP;
	static BufferedWriter outT;
	static BufferedWriter outTime;
	static BufferedWriter outTime2;
	static BufferedWriter outB;
	static BufferedWriter outPred;
	static BufferedWriter outS;
	static BufferedWriter outES;

	static BufferedWriter outMA;
 
	static BufferedWriter outRL;

	static String extension = ".txt";

	static String root = "Results/Details/";

	public static String timeStamp;

	public static Vector<Integer> convertToVector(int[] basket) {

		Vector<Integer> myTrade = new Vector<Integer>();

		for (int i = 0; i < basket.length; i++) {
			myTrade.addElement(basket[i]);

		}
		return myTrade;
	}

	public static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isDouble(String input) {
		try {
			Double.parseDouble(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	//reporting purpose
	public static void openTimeFile() {
		String timeStamp = Utility.timeStamp + "(" + U.fileInfo + ")";
		try {

			File fileTime = null;
			if (U.writeTimefile) {
				fileTime = new File(root + timeStamp + "-Time-" + extension);

				FileWriter fstream2 = new FileWriter(fileTime);
				outTime = new BufferedWriter(fstream2);

				String output = "Detail" + U.delim
						+ "Time ( nano ->*POWER(10,-9))\n";

				outTime.write(output);

			}

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage() + " \n"
					+ e.toString());
		}

	}
	//reporting purpose
	public static void openFiles(MarketMaker mm) {
		try {

			String timeStamp = Utility.timeStamp + "(" + U.fileInfo + ")";

			// String root= "Results/"+timeStamp+"/";

			// Create file
			File file = new File(root + timeStamp + "-R-" + extension);
			FileWriter fstream = new FileWriter(file);
			outR = new BufferedWriter(fstream);

			String output = "MarketDate" + U.delim + "correct answer" + U.delim
					+ "Market Prediction" + U.delim + "Error" + U.delim;

			Iterator<AbstractAgent> iterator = mm.getParticipants().iterator();
			while (iterator.hasNext()) {

				output += "Error: "
						+ ((AbstractAgent) iterator.next()).getAgentName()
						+ U.delim;
			}

			iterator = mm.getParticipants().iterator();
			while (iterator.hasNext()) {

				output += "capital: "
						+ ((AbstractAgent) iterator.next()).getAgentName()
						+ U.delim;
			}

			iterator = mm.getParticipants().iterator();
			while (iterator.hasNext()) {

				output += "Revenue: "
						+ ((AbstractAgent) iterator.next()).getAgentName()
						+ U.delim;
			}

			iterator = mm.getParticipants().iterator();
			while (iterator.hasNext()) {

				output += "Invest: "
						+ ((AbstractAgent) iterator.next()).getAgentName()
						+ U.delim;
			}

			output += "\n";
			outR.write(output);

			// Create file
			File fileTime2 = null;
			if (U.writeTimefile) {
				fileTime2 = new File(root + timeStamp + "-Time2-" + extension);

				FileWriter fstream2 = new FileWriter(fileTime2);
				outTime2 = new BufferedWriter(fstream2);

				output = "Market Number" + U.delim;

				iterator = mm.getParticipants().iterator();
				while (iterator.hasNext()) {

					output += "time(nano): "
							+ ((AbstractAgent) iterator.next()).getAgentName()
							+ U.delim;
				}

				outTime2.write(output + "\n");

			}

			// Create file outRL
			if (U.writeRlFile) {
				File fileoutRL = new File(root + timeStamp + "-outRL-"
						+ extension);
				FileWriter fstreamoutRL = new FileWriter(fileoutRL);
				outRL = new BufferedWriter(fstreamoutRL);
				output = "\n";
				outRL.write(output);
			}

			// Create file P
			File fileP = new File(root + timeStamp + "-P-" + extension);
			FileWriter fstreamP = new FileWriter(fileP);
			outP = new BufferedWriter(fstreamP);

			output = "MarketDate" + U.delim + "correct answer" + U.delim
					+ "Market Prediction" + U.delim;

			iterator = mm.getParticipants().iterator();

			while (iterator.hasNext()) {

				output += "Pr:"
						+ ((AbstractAgent) iterator.next()).getAgentName()
						+ U.delim;
			}

			output += "\n";
			outP.write(output);

			// create File Multi Attribute info
			File fileMA = null;
			if (U.writeMAfile) {
				fileMA = new File(root + timeStamp + "-MA-" + extension);
				FileWriter fstreamMA = new FileWriter(fileMA);
				outMA = new BufferedWriter(fstreamMA);
				outMA.write("Agents Feeding data \n");

			}

			File fileT = null;

			if (U.writeTfile) {
				fileT = new File(root + timeStamp + "-T-" + extension);

				FileWriter fstream2 = new FileWriter(fileT);
				outT = new BufferedWriter(fstream2);

				output = "MarketDate" + U.delim + "Day" + U.delim
						+ "Agent Name" + U.delim + "Note" + U.delim;

				output += "Cost" + U.delim + "Capital" + U.delim;
				output += "\n";

				outT.write(output);

			}

			File fileB = null;
			if (U.writeBfile) {
				fileB = new File(root + timeStamp + "-B-" + extension);

				FileWriter fstream3 = new FileWriter(fileB);
				outB = new BufferedWriter(fstream3);

				output = "MarketDate" + U.delim + "Day" + U.delim
						+ "Agent Name" + U.delim;
				output += "Prediction" + U.delim + "Invest" + U.delim
						+ " New Capital" + U.delim + " Error";// +U.delim+"Prediction"+U.delim+"Invest"+U.delim+"Prediction"+U.delim+"Invest"+U.delim+"Prediction"+U.delim+"Invest";

				output += "\n";

				outB.write(output);

			}

			File filePred = null;
			if (U.writePredfile) {
				filePred = new File(root + timeStamp + "-Pred-" + extension);

				FileWriter fstream3 = new FileWriter(filePred);
				outPred = new BufferedWriter(fstream3);

				output = "MarketDate" + U.delim;

				for (int i = U.minMITrheshold; i < U.maxMITrheshold; i = i
						+ U.stepMITrheshold)
					output += "treshold=" + i + "%" + U.delim;

				output += "\n";
				outPred.write(output);

			}

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage() + " \n"
					+ e.toString());
		}

	}
	//reporting purpose
	public static void closeFiles() {
		try {

			// Close the output stream
			outR.close();
			outP.close();


			if (U.writeRlFile)
				outRL.close();

			if (U.writeMAfile)
				outMA.close();

			if (U.writeTfile)
				outT.close();

			if (U.writeTimefile)
				outTime.close();

			if (U.writeTime2file)
				outTime2.close();

			if (U.writeBfile)
				outB.close();

			if (U.writePredfile)
				outPred.close();

			outS.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
	//reporting purpose
	public static void WriteToReportR(String str) {

		try {

			outR.write(str);

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	//reporting purpose
	public static void WriteToReportMA(String str) {
		if (U.writeMAfile) {
			try {

				outMA.write(str);

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}

	}
	//reporting purpose
	public static void WriteToReportP(String str) {

		try {

			outP.write(str);

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	
	//reporting purpose
	public static void WriteToReportSummary(String str, MarketMaker mm) {

		
		try {

			File file4 = new File("Results/" + "summary.txt");

			String output = "";
			if (!file4.exists()) {
				file4.createNewFile();

				output = "Notes" + U.delim + "Experiment" + U.delim
						+ "Number of Agents" + U.delim;
				output += "Trade Strategy" + U.delim ;
				output += "Number of Days" + U.delim ;
				output += " Number of Records" + U.delim;
				output += "MaxRPT1" + U.delim;
				output += "MaxRPT2" + U.delim;
				output += "MinRPT2" + U.delim;
				
				output += "MAE" + U.delim;
				output += "MSE" + U.delim;

			 
 

			}

			FileWriter fstream4 = new FileWriter(file4, true);
			outS = new BufferedWriter(fstream4);
			outS.write(output);

			outS.write(str);

			outS.close();

		} catch (Exception e) {
			System.err.println("Error: hereeeee " + e.getMessage());
			System.out.println("Error: hereeeee " + e.getMessage());
		}

	}
	//reporting purpose
	public static void WriteToReportExtraSummary(String str) {

		try {

			File file5 = new File("Results/" + "ExraSummary.txt");

			String output = "";
			if (!file5.exists()) {
				file5.createNewFile();

				output = "Number of Runs" + U.delim + "Number of Agents"
						+ U.delim;
				output += "Agents" + U.delim + "Capital" + U.delim;
				output += "Number of Days" + U.delim + "Confidence" + U.delim;
				output += "  Missclassification Rate" + U.delim + "Accuracy"
						+ U.delim + " Precision" + U.delim;
				output += "  Recall" + U.delim + "f_Score" + U.delim
						+ " Sensitivity" + U.delim + " Specificity" + U.delim;
				output += "  TP" + U.delim + "FP" + U.delim + "TN" + U.delim
						+ " FN" + U.delim + " Agents:Confidence:Capital ";
				output += " Number of Securities" + U.delim
						+ " Number of Records" + U.delim;

				// output+="\n";
			}

			FileWriter fstream5 = new FileWriter(file5, true);
			outES = new BufferedWriter(fstream5);
			outES.write(output);
			outES.write(str);
			outES.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
	//reporting purpose
	public static void WriteActionStatistic() {
		if (U.WriteActionStatistic == true) {

			String output = U.getActionStatistic();

			try {

				// open file4

				File file5 = new File(root + timeStamp + "-ActionStatistic-"
						+ extension);

				FileWriter fstream5 = new FileWriter(file5, true);
				outES = new BufferedWriter(fstream5);
				outES.write(output);
				outES.close();

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}

		}

	}
	//reporting purpose
	public static void WriteDataFile(Vector<Data> allData) {

		try {

			// open file4

			File file5 = new File(root + timeStamp + "-Synthetic-" + extension);

			FileWriter fstream5 = new FileWriter(file5, true);
			outES = new BufferedWriter(fstream5);
			outES.write("Correct Ans" + U.delim + "a5Data" + U.delim + "a5Data"
					+ U.delim + "\n");

			String Output = "";
			for (int i = 0; i < allData.size(); i++) {
				Vector<Double> valueList = allData.elementAt(i).getValueList();
				Output = allData.elementAt(i).getValue() + U.delim;
				for (int j = 0; j < valueList.size(); j++) {
					Output += valueList.elementAt(j) + U.delim;
				}
				outES.write(Output + "\n");
			}

			outES.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}
	//reporting purpose
	public static void WriteActionStatistic2() {
		if (U.WriteActionStatistic2 == true) {

			String output = U.getActionStatistic2();

			try {
				File file5 = new File(root + timeStamp + "-ActionStatistic2-"
						+ extension);

				FileWriter fstream5 = new FileWriter(file5, true);
				outES = new BufferedWriter(fstream5);
				outES.write(output);
				outES.close();

			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}

		}

	}
	//reporting purpose
	public static void SummaryforParams(Vector<Double> error,
			Vector<Double> ErrorForTraining, Vector<Double> ErrorForTesting) {

		try {

			File file5 = new File("Results/" + "SummaryforParams.txt");

			String output = "";
			if (!file5.exists()) {
				file5.createNewFile();

				output += "Beta" + U.delim + "RevenueCuttOffParam" + U.delim
						+ "RevenuePower" + U.delim + "ratePerTransaction"
						+ U.delim + "U.marketIntegrationMethod";
				output += U.delim + "marketIntegrationMethodTreshold" + U.delim
						+ "Total Error" + U.delim + "Trianing Error" + U.delim
						+ "Testing Error";

				output += "\n";
			}

			double threshold = U.minMITrheshold;

			for (int i = 0; i < error.size(); i++) {
				output += U.beta + U.delim + U.RevenueCuttOffParam + U.delim
						+ U.RevenuePower + U.delim + U.maxRatePerTransaction
						+ U.delim;
				output += U.marketIntegrationMethod + U.delim + threshold
						+ U.delim + error.elementAt(i) + U.delim
						+ ErrorForTraining.elementAt(i) + U.delim
						+ ErrorForTesting.elementAt(i);
				output += "\n";

				threshold += U.stepMITrheshold;
			}

			FileWriter fstream5 = new FileWriter(file5, true);
			BufferedWriter outSfp = new BufferedWriter(fstream5);
			outSfp.write(output);
			outSfp.close();

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

	}
	//reporting purpose
	public static void WriteToReportT(String str) {

		if (U.writeTfile) {
			try {

				outT.write(str);

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
	}
	//reporting purpose
	public static void WriteToReportTime(String str) {

		if (U.writeTimefile) {
			try {

				outTime.write(str + "\n");

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	//reporting purpose
	public static void WriteToReportTime2(String str) {

		if (U.writeTimefile) {
			try {

				outTime2.write(str);

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	public static void WriteToReportQ(String str) {

	}
	//reporting purpose
	public static void WriteToReportB(String str) {
		if (U.writeBfile) {

			try {

				outB.write(str);

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}

	}

	//reporting purpose
	public static void WriteToReportPred(String date, Vector<Double> marketPred) {

		if (U.writePredfile) {
			String str = date;

			for (int i = 0; i < marketPred.size(); i++)
				str += U.delim + marketPred.elementAt(i);

			str += "\n";
			try {

				outPred.write(str);

			} catch (Exception e) {// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}
		}
	}

	 

 

}
