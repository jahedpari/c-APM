package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.trees.M5P;

public class M5P_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new M5P();

	public M5P_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
