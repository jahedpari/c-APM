package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.DecisionStump;

public class DecisionStump_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new DecisionStump();

	public DecisionStump_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
