package prediction.wekaPrediction;

import java.util.Vector;
import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.Vote;

public class Vote_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new Vote();

	public Vote_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
