package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;

import market.MarketMaker;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.lazy.IBk;

public class IBK_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new IBk();

	public IBK_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
