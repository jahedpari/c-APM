package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;

import market.MarketMaker;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.functions.MultilayerPerceptron;

public class MultilayerPerceptron_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new MultilayerPerceptron();

	public MultilayerPerceptron_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
