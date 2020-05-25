import java.util.Calendar;
import Utill.U;
import Utill.Utility;
import market.MarketMaker;
 
public class Main {

	static MarketMaker mm;

	 

	public static void main(String[] args) {

		System.out.println("بسم الله الرحمن الرحيم");
		
		Calendar cal = Calendar.getInstance();
		Utility.timeStamp = cal.getTimeInMillis() + "";
		Utility.openTimeFile();
		long startTime = System.nanoTime();
		Utility.WriteToReportTime("start  experiment " + U.delim + startTime);
		mm = new MarketMaker();
		Execute.Run(mm);

		long stopTime = System.nanoTime();

		double timeSpent = (stopTime - startTime) / 1000000000.0;
		System.out.println("time taken to finish the experiment  " + timeSpent+ " sec");
		Utility.WriteToReportTime("time taken to finish the experiment " + U.delim + timeSpent);

	}

}