package prediction.wekaPrediction;

import java.util.Vector;

import market.MarketMaker;
import Data.Data;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.Stacking;

public class Stacking_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new Stacking();

	public Stacking_WekaPrediction(MarketMaker mm) {

		super(mm);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
