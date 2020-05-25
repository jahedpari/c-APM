package prediction.wekaPrediction;

import java.util.Vector;
import prediction.PredictionModel;
import Utill.MyAttribute;
import weka.classifiers.AbstractClassifier;
import weka.core.DenseInstance;
import weka.core.Instances;
import Utill.SimDate;
import Utill.U;
import Utill.Utility;
import Data.Data;
import market.MarketMaker;

public class WekaPrediction extends PredictionModel {

	protected Vector<Data> allData;
	Vector<Double> currentData;

	AbstractClassifier model;
	Instances instances;
	DenseInstance currentInst;

	public String getModel() {

		return model.toString();
	}

	public WekaPrediction(MarketMaker mm) {
		super(mm);

		allData = new Vector<Data>();

		instances = new Instances("TrainInstances", U.atts, 0);

		instances.setClassIndex(0);

	}

	public void setCalssifier(AbstractClassifier ml) {
		model = ml;
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getClassifierName() {

		return ("Weka_" + model.getClass().getSimpleName());
	}

	@Override
	public void reset() {
		this.allData = new Vector<Data>();

		instances = new Instances("TrainInstances", U.atts, 0);

	}

	public double predict(Data data, SimDate marketDate) {
		// U.p("start: predict");
		double prediction = 0;

		currentData = data.getValueList();

		currentInst = MyAttribute.makeTestInstance(currentData);
		currentInst.setDataset(instances);

		try {

			// model.buildClassifier(instances);
			if (U.marketNumber > 1)
				prediction = model.classifyInstance(currentInst);

		} catch (Exception e) {
			U.p(e.toString());
			e.printStackTrace();

		}

		predictionValue = prediction;

		return predictionValue;
	}

	/* it is update after wining security is revealed by RL agent */
	public void updateBelief(double winingsecurityIndex) {

		long startTime = System.nanoTime();
		currentInst.setClassValue(winingsecurityIndex);
		instances.add(currentInst);

		try {

			// model.buildClassifier(instances);
			model.buildClassifier(instances);
			// ((UpdateableClassifier) model).updateClassifier(currentInst);

		} catch (Exception e) {

			e.printStackTrace();
		}

		long stopTime = System.nanoTime();

		double timeSpent = (stopTime - startTime) / 1000000000.0;

		Utility.WriteToReportTime2(timeSpent + U.delim);
		// U.p("end: updateBelief");
	}

	/*
	 * in case of doing sequence mining, put numberOfElem >=1 in case of doing
	 * multi-attribute, put numberOfElem =-1
	 */
	Vector<Double> getDataMoreElem(Vector<Data> dataList, SimDate marketDate,
			int numberOfElem) {
 

		Vector<Double> ans = new Vector<Double>();
		allData = dataList;

		double value = 0;
		Vector<Double> values = null;

		int index = 0;
		int counter = 0;
		boolean flag = false;

		for (Data data : allData) {
			if (data.getMonitoringDay().beforeOrEqual(marketDate)) {

				if (data.getMonitoringDay().isEqual(marketDate)) {

					if (data.getValueList() != null)
						values = data.getValueList();
					else
						value = data.getValue();

					flag = true;
					index = counter;
					break;
				}
			}

			// continue until you pass the monitoring date
			else if (data.getMonitoringDay().after(marketDate)) {

				if (flag == false) {
					index = counter - 1;

					System.out
							.println(this.getClass().getSimpleName()
									+ "is saying: the Exact date is not found. we used the nearest one: "
									+ allData.elementAt(index)
											.getMonitoringDay());
				}

				flag = true;
				break;
			}

			counter = counter + 1;
		}

		if (flag == false) {

			try {
				throw new Exception("could not predict.sorry! I am so dumb.");
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		// U.p("end: getDataMoreElem");
		if (values != null)
			return values;

		else if (numberOfElem == -1) {
			ans.add(value);
			return ans;
		} else {// adding the value of the event date and value of the 4
				// preceding date
			for (int i = 0; i < numberOfElem; i++) {
				if ((index - i) >= 0)
					ans.add(allData.elementAt(index - i).getValue());
				else
					ans.add(0.0);
			}
			 

			return ans;

		}

	}

}
