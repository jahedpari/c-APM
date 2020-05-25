package prediction.wekaPrediction;

import java.util.Vector;

import Data.Data;
import market.MarketMaker;
import weka.classifiers.AbstractClassifier;

import weka.classifiers.trees.REPTree;

public class REPTree_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new REPTree();

	public REPTree_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
