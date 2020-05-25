package Utill;

import java.util.ArrayList;
import java.util.Vector;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

public class MyAttribute {

	static ArrayList<Attribute> attributeList;

	static int size;

	static public void reset() {

		attributeList = new ArrayList<Attribute>();

	}

	static private void addNumericAttribute(String label) {

		Attribute attribute = new Attribute(label);
		attributeList.add(attribute);

	}

	// creating attributes
	static public ArrayList<Attribute> makeDefault_r(int Size) {
		reset();
		attributeList = new ArrayList<Attribute>(size);
		size = Size + 1; // to add label

		addNumericAttribute("CorrectAns:");

		for (int i = 1; i < size; i++)
			addNumericAttribute("WeekData:" + i);

		return attributeList;

	}

	public static Instance makeTrainingInstance(int correctValue, double value) {

		DenseInstance instance = new DenseInstance(size);

		instance.setValue(0, correctValue);
		instance.setValue(1, value);

		return instance;

	}

	public static Instance makeTrainingInstance(double correctValue,
			Vector<Double> data) {

		DenseInstance instance = new DenseInstance(size);

		instance.setValue(0, correctValue);
		for (int i = 0; i < data.size(); i++)
			instance.setValue(i + 1, data.elementAt(i));

		return instance;

	}

	public static Instance makeTestInstance(double data) {

		DenseInstance instance = new DenseInstance(size);

		instance.setValue(1, data);
		instance.setMissing(0);

		return instance;

	}

	public static DenseInstance makeTestInstance(Vector<Double> data) {

		DenseInstance instance = new DenseInstance(size);

		instance.setMissing(0);

		for (int i = 0; i < data.size(); i++) {

			instance.setValue(i + 1, data.elementAt(i));

		}

		return instance;

	}

	public static Instance makeInstance1(int labelIndex, double[] data) {

		DenseInstance instance = new DenseInstance(size);

		instance.setValue(0, labelIndex);
		for (int i = 0; i < data.length; i++)
			instance.setValue(i + 1, data[i]);

		return instance;

	}

}
