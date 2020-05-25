package Data;



import java.util.Vector;

import Utill.SimDate;

public class Data {

	private SimDate accessDay;
	private SimDate monitoringDay;
	private double value;
	private String level;
	private Vector<Double> valueList;


	public Data(SimDate monitoringDay, double value,SimDate accessDay) {
		super();
		this.accessDay = accessDay;
		this.monitoringDay = monitoringDay;
		this.value = value;
		this.level="null";
		this.valueList=null;
		
	}
	
	public Data(SimDate monitoringDay, String level,SimDate accessDay) {
		super();
		this.accessDay = accessDay;
		this.monitoringDay = monitoringDay;
		this.value = -1;
		this.level=level.replace("\"", "");
		this.valueList=null;
	}
	
	
	public Data(SimDate monitoringDay, Vector<Double> value,SimDate accessDay, String level) {
		super();
		this.accessDay = accessDay;
		this.monitoringDay = monitoringDay;
		this.value = -1;
		this.level=level.replace("\"", "");
		this.valueList=value;
		
	}
	
	public Data(SimDate monitoringDay, Vector<Double> value,SimDate accessDay, String level, double CorrectValue) {
		super();
		this.accessDay = accessDay;
		this.monitoringDay = monitoringDay;
		this.value = CorrectValue;
		this.level=level.replace("\"", "");
		this.valueList=value;
		
	}
	
	public Data(SimDate monitoringDay, Vector<Double> value,SimDate accessDay) {
		super();
		this.accessDay = accessDay;
		this.monitoringDay = monitoringDay;
		this.value = -1;
		this.level="null";
		this.valueList=value;
		
	}
	
/*	public Data(SimDate monitoringDay, double value) {
		super();
		this.accessDay = null;
		this.monitoringDay = monitoringDay;
		this.value = value;
	}
*/
	public SimDate getAccessDay() {
		return accessDay;
	}

	public void setAccessDay(SimDate accessDay) {
		this.accessDay = accessDay;
	}

	public SimDate getMonitoringDay() {
		return monitoringDay;
	}

	public void setMonitoringDay(SimDate monitoringDay) {
		this.monitoringDay = monitoringDay;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
	
	
	public void print()
	{
		System.out.print(monitoringDay+" "+value+" "+level+" "+ accessDay+"    ");
		
		if(valueList != null)
		{
		for(int i=0; i<valueList.size(); i++)
			System.out.print(valueList.elementAt(i)+" ");
		System.out.println("");
		}
	}

	public Vector<Double> getValueList() {
		return valueList;
	}

	public void setValueList(Vector<Double> valueList) {
		this.valueList = valueList;
	}
	
	
}
