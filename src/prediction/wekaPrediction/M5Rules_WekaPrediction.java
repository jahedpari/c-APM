package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.rules.M5Rules;

public class M5Rules_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new M5Rules();

	public M5Rules_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
