package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;

import market.MarketMaker;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.trees.RandomForest;

public class RandomForest_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new RandomForest();

	public RandomForest_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
