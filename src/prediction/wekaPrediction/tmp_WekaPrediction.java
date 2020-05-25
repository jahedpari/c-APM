package prediction.wekaPrediction;

import java.util.Vector;
import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.trees.DecisionStump;

public class tmp_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new DecisionStump();

	public tmp_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
