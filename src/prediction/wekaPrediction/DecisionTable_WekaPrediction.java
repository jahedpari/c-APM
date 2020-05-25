package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.rules.DecisionTable;

public class DecisionTable_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new DecisionTable();

	public DecisionTable_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
