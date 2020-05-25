package Utill;

public class Bid {

	double prediction;
	double invest;

	public Bid(double Prediction, double Invest) {
		prediction = Prediction;
		invest = Invest;

	}

	public double getPrediction() {
		return prediction;
	}

	public void setPrediction(double prediction) {
		this.prediction = prediction;
	}

	public double getInvest() {
		return invest;
	}

	public void setInvest(double invest) {
		this.invest = invest;
	}
}
