package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.lazy.LWL;

public class LWL_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new LWL();

	public LWL_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
