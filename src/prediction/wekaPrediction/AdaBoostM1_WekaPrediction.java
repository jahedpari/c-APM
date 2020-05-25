package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;
import market.MarketMaker;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.AdaBoostM1;

public class AdaBoostM1_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new AdaBoostM1();

	public AdaBoostM1_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

		// if( U.marketNumber >= 1)
		// ((IBK) classifier).reset();

		// classifier.resetLearning();

	}

}
