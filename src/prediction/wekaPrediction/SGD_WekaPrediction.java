package prediction.wekaPrediction;

import java.util.Vector;
import Data.Data;
import market.MarketMaker;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.SGD;
import weka.core.SelectedTag;

public class SGD_WekaPrediction extends WekaPrediction {

	AbstractClassifier classifier = new SGD();

	public SGD_WekaPrediction(MarketMaker mm) {

		super(mm);

		((SGD) classifier).setLossFunction(new SelectedTag(SGD.SQUAREDLOSS,
				SGD.TAGS_SELECTION));

		((SGD) classifier).setLearningRate(1.0E-4);

		setCalssifier(classifier);
	}

	public void reset() {
		this.allData = new Vector<Data>();

	}

}
