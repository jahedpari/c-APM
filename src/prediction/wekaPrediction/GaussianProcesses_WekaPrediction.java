package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;
import market.MarketMaker;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.GaussianProcesses;

public class GaussianProcesses_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new GaussianProcesses();

	public GaussianProcesses_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
