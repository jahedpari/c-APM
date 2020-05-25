package prediction;

import market.MarketMaker;
import Data.Data;
import Utill.SimDate;

public abstract class PredictionModel {

	protected double predictionValue;

	public void setPredictionValue(double predictionValue) {
		this.predictionValue = predictionValue;
	}

	public String getModel() {
		return "In prediction model Class is null. Sorry";
	}

	protected String predictionSecurity;

	protected static MarketMaker marketMaker;

	protected PredictionModel(MarketMaker mm) {
		predictionValue = -1000;
		marketMaker = mm;
		predictionSecurity = null;
	}

	public abstract void reset();

	public double getPredictionValue() {
		return predictionValue;
	}

	public String getpredictionSecurity() {
		return predictionSecurity;
	}

	public double predict(Data myData, SimDate marketDate) {
		return -1;
	}

	public double predict(Data myData, SimDate marketDate, Boolean elementAt) {
		return -1;
	}

	public void updateBelief(double winnerValue) {
	}

}
