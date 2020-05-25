package prediction;

import prediction.PredictionModel;
import Utill.SimDate;
import Data.Data;
import market.MarketMaker;

public class simplePrediction extends PredictionModel {

	double currentData;

	public simplePrediction(MarketMaker mm) {
		super(mm);

	}

	@Override
	public void reset() {

	}

	public double predict(Data data, SimDate marketDate) {



		currentData = data.getValueList().elementAt(0);

		predictionValue = currentData;

		// Utility.WriteToReportQ("marketDate:" +marketDate+ U.delim);

		return predictionValue;
	}

	/* it is update after wining security is revealed by RL agent */
	public void updateBelief(double winingsecurityIndex) {

	}

}
