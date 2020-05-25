package Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import market.MarketMaker;
import Utill.MyException;
import Utill.SimDate;
import Utill.U;
import Utill.Utility;

public class DataDistributer {

	static Vector<Data> allData = new Vector<Data>();

	// taking the values for the index, we start from 4, since 0 is date, 1 is
	// true value, 2 is true label, 3 is access date
	Vector<Double> valueList = new Vector<Double>();

	protected static MarketMaker mm;
	int rows;
	int cols;
	private int[][] agentDataAccessIndex;

	// it reads the whole file to fills allData vector
	// in input file, 1st column must be date, 2nd true value, 3rd true label,
	// 4th access date
	public void reset() {
		allData = new Vector<Data>();
		valueList = new Vector<Double>();
		mm = null;
		agentDataAccessIndex = null;

	}

	@SuppressWarnings("resource")
	public DataDistributer(String inputFile, MarketMaker marketMaker) {
		reset();
		mm = marketMaker;

		// U.p("mm in dd2  "+mm.toString());

		rows = mm.getParticipants().size();
		cols = U.maxSizeforAgentDataAccess;
		U.p("\n***cols: " + cols);

		agentDataAccessIndex = new int[rows][cols];

		// initialize the agentDataAccessIndex array
		for (int agentIndex = 0; agentIndex < rows; agentIndex++) {
			for (int j = 0; j < cols; j++) {
				// find column numbers where the agent have access to
				agentDataAccessIndex[agentIndex][j] = -1;

			}
		}

		// read file and fill allData
		String line = null;

		try {

			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] result = line.split("\\s");// line.split(",");

				// U.p("result.length: "+result.length
				// +"  U.firstAgentNumber-1: "+ (U.firstAgentNumber-1) );
				if (result.length >= U.firstColumnNumber - 1) {

					// the first column should show when event date/ monitoring
					// date
					SimDate monitorDate = SimDate.convertToDate(result[0]);
					Data data = null;

					if (monitorDate != null) {

						SimDate accessDate = null;

						// taking the values for the index, we start from 4,
						// since 0 is date, 1 is true value, 2 is true label 
						// 3 is access date
						Vector<Double> valueList = new Vector<Double>();
						for (int i = U.firstColumnNumber - 1; i < result.length; i++) {

							if (Utility.isDouble(result[i]))
								valueList.add(Double.parseDouble(result[i]));
							else
								valueList.add(Double.POSITIVE_INFINITY);
						}

						double correctValue = Double.parseDouble(result[1]);

						data = new Data(monitorDate, valueList, accessDate,
								result[2], correctValue);

						allData.add(data);
					}

				} else

					throw new MyException(
							" Number of elements are not smaller than expected"
									+ inputFile);

			}

			bufferedReader.close();

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");

			try {
				throw new MyException("File is not found " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println(ex.toString());
			System.out.println("Error reading file '" + inputFile + "'");
			try {
				throw new MyException("File error " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();

			}

		} catch (MyException e) {

			e.printStackTrace();

		}

	}

	// DataGenerator and Distributer for synthetic data
	@SuppressWarnings("resource")
	public DataDistributer(String inputFile, MarketMaker marketMaker,
			int[] quantity, double[] noise) {
		reset();
		mm = marketMaker;

		rows = mm.getParticipants().size();
		cols = U.maxSizeforAgentDataAccess;
		U.p("\n***cols: " + cols);

		// tp say which agent reads which columns
		agentDataAccessIndex = new int[rows][cols];

		// initialize the agentDataAccessIndex array
		for (int agentIndex = 0; agentIndex < rows; agentIndex++) {
			for (int j = 0; j < cols; j++) {
				// find column numbers where the agent have access to
				agentDataAccessIndex[agentIndex][j] = -1;

			}
		}

		// read file and fill allData
		String line = null;

		try {

			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] result = line.split("\\s"); 

 
				if (result.length >= U.firstColumnNumber - 1) {

					// the first column should show when event date 
					SimDate monitorDate = SimDate.convertToDate(result[0]);
					Data data = null;

					if (monitorDate != null) {

						SimDate accessDate = null;

						double correctValue = Double.parseDouble(result[1]);

						// taking the values for the index, we start from 4,
						// since 0 is date, 1 is true value, 2 is true label 
						// 3 is access date
						// read data of each data column
						Vector<Double> valueList = new Vector<Double>();

						int counter = 0;
						for (int i = 0; i < quantity.length; i++) {

							int quan = quantity[i];
							double noiseRate = noise[i];

							for (int j = 0; j < quan; j++) {

								Random rand = new Random();

								double max = correctValue + noiseRate
										* correctValue;
								double min = correctValue - noiseRate
										* correctValue;
								double value = rand.nextDouble() * (max - min)
										+ min;

								valueList.add(value);

								counter++;
							}

							int maxCounter = counter + U.firstColumnNumber - 1;

							if (maxCounter > result.length)
								throw new Error(
										"we are generating data columns more than expected"
												+ "counter=" + counter
												+ " maxCounter" + maxCounter
												+ " result.length"
												+ result.length);

						}

						data = new Data(monitorDate, valueList, accessDate,
								result[2], correctValue);

						// data.print();

						allData.add(data);
					}

				} else

					throw new MyException(
							" Number of elements are not smaller than expected"
									+ inputFile);

			}

			bufferedReader.close();
			Utility.WriteDataFile(allData);

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");

			try {
				throw new MyException("File is not found " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println(ex.toString());
			System.out.println("Error reading file '" + inputFile + "'");
			try {
				throw new MyException("File error " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();

			}

		} catch (MyException e) {

			e.printStackTrace();
		}

	}

	// creates one agent from file and for quantity numbers agent with random number
	@SuppressWarnings("resource")
	public DataDistributer(String inputFile, MarketMaker marketMaker,
			int quantity) {
		reset();
		mm = marketMaker;

		rows = mm.getParticipants().size();
		cols = U.maxSizeforAgentDataAccess;
		U.p("\n***cols: " + cols);

		// to say which agent reads which columns
		agentDataAccessIndex = new int[rows][cols];

		// initialize the agentDataAccessIndex array
		for (int agentIndex = 0; agentIndex < rows; agentIndex++) {
			for (int j = 0; j < cols; j++) {
				// find column numbers where the agent have access to
				agentDataAccessIndex[agentIndex][j] = -1;

			}
		}

		// read file and fill allData
		String line = null;

		try {

			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] result = line.split("\\s");

				if (result.length >= U.firstColumnNumber - 1) {

					// the first column should show when event date/ monitoring
					// date
					SimDate monitorDate = SimDate.convertToDate(result[0]);
					Data data = null;

					if (monitorDate != null) {

						SimDate accessDate = null;

						double correctValue = Double.parseDouble(result[1]);

						// taking the values for the index, we start from 4,
						// since 0 is date, 1 is true value, 2 is true label 
						// 3 is access date
						// read data of each data column
						Vector<Double> valueList = new Vector<Double>();

						int i = U.firstColumnNumber - 1;
						{

							if (Utility.isDouble(result[i]))
								valueList.add(Double.parseDouble(result[i]));
							else
								valueList.add(Double.POSITIVE_INFINITY);

						}

						for (int j = 0; j < quantity; j++) {

							Random rand = new Random();

							double value = rand.nextDouble();

							valueList.add(value);

						}

						data = new Data(monitorDate, valueList, accessDate,
								result[2], correctValue);

						allData.add(data);
					}

				} else

					throw new MyException(
							" Number of elements are not smaller than expected"
									+ inputFile);

			}

			bufferedReader.close();
			Utility.WriteDataFile(allData);

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");

			try {
				throw new MyException("File is not found " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println(ex.toString());
			System.out.println("Error reading file '" + inputFile + "'");
			try {
				throw new MyException("File error " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();

			}

		} catch (MyException e) {

			e.printStackTrace();
		}

	}

	// DataGenerator and Distributer for synthetic data
	@SuppressWarnings("resource")
	public DataDistributer(String inputFile, MarketMaker marketMaker,
			int[] quantity, double[] noise, int ChangeQualityCounter,
			boolean ChangeQuality) {

		reset();
		mm = marketMaker;

		rows = mm.getParticipants().size();
		cols = U.maxSizeforAgentDataAccess;
		U.p("\n***cols: " + cols);

		// to say which agent reads which columns
		agentDataAccessIndex = new int[rows][cols];

		// initialize the agentDataAccessIndex array
		for (int agentIndex = 0; agentIndex < rows; agentIndex++) {
			for (int j = 0; j < cols; j++) {
				// find column numbers where the agent have access to
				agentDataAccessIndex[agentIndex][j] = -1;

			}
		}

		// read file and fill allData
		String line = null;

		try {

			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			int recordCounter = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] result = line.split("\\s");// line.split(",");

				if (result.length >= U.firstColumnNumber - 1) {

					// the first column should show when event date/ monitoring
					// date
					SimDate monitorDate = SimDate.convertToDate(result[0]);
					Data data = null;

					if (monitorDate != null) {
						recordCounter++;
						U.p("recordCounter: " + recordCounter);
						outer: if (ChangeQuality == true) {
							if ((recordCounter % ChangeQualityCounter) == 0) {

								int index = recordCounter
										/ ChangeQualityCounter - 1;
								if (index == noise.length) {
									ChangeQuality = false;
									break outer;
								}

								noise[index] = 1;
								U.p("noise for index: " + index
										+ " is changed to 1");

							}
						}

						SimDate accessDate = null;

						double correctValue = Double.parseDouble(result[1]);

						// taking the values for the index, we start from 4,
						// since 0 is date, 1 is true value, 2 is true label, 3
						// is access date
						// read data of each data column
						Vector<Double> valueList = new Vector<Double>();
						int counter = 0;
						for (int i = 0; i < quantity.length; i++) {

							int quan = quantity[i];
							double noiseRate = noise[i];

							for (int j = 0; j < quan; j++) {

								double value = correctValue + noiseRate;

								valueList.add(value);

								counter++;
							}

							int maxCounter = counter + U.firstColumnNumber - 1;

							if (maxCounter > result.length)
								throw new Error(
										"we are generating data columns more than expected"
												+ "counter=" + counter
												+ " maxCounter" + maxCounter
												+ " result.length"
												+ result.length);

						}

						data = new Data(monitorDate, valueList, accessDate,
								result[2], correctValue);

						// data.print();

						allData.add(data);
					}

				} else

					throw new MyException(
							" Number of elements are not smaller than expected"
									+ inputFile);

			}

			bufferedReader.close();

			Utility.WriteDataFile(allData);

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");

			try {
				throw new MyException("File is not found " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println(ex.toString());
			System.out.println("Error reading file '" + inputFile + "'");
			try {
				throw new MyException("File error " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();

			}

		} catch (MyException e) {

			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	public DataDistributer(String inputFile, MarketMaker marketMaker,
			int[] quantity, double[] noise, int window) {
		reset();
		mm = marketMaker;

		rows = mm.getParticipants().size();
		cols = U.maxSizeforAgentDataAccess;
		U.p("\n***cols: " + cols);

		// to say which agent reads which columns
		agentDataAccessIndex = new int[rows][cols];

		// initialize the agentDataAccessIndex array
		for (int agentIndex = 0; agentIndex < rows; agentIndex++) {
			for (int j = 0; j < cols; j++) {
				// find column numbers where the agent have access to
				agentDataAccessIndex[agentIndex][j] = -1;

			}
		}

		// read file and fill allData
		String line = null;

		try {

			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int windowCounter = 1;
			int windowIndex = 0;
			while ((line = bufferedReader.readLine()) != null) {
				String[] result = line.split("\\s");// line.split(",");

				if (result.length >= U.firstColumnNumber - 1) {

					// the first column should show when event date/ monitoring
					// date
					SimDate monitorDate = SimDate.convertToDate(result[0]);
					Data data = null;

					if (monitorDate != null) {

						SimDate accessDate = null;
						// SimDate accessDate =
						// SimDate.convertToDate(result[3]);

						double correctValue = Double.parseDouble(result[1]);

						// taking the values for the index, we start from 4,
						// since 0 is date, 1 is true value, 2 is true label, 3
						// is access date
						// read data of each data column
						Vector<Double> valueList = new Vector<Double>();

						int counter = 0;
						for (int i = 0; i < quantity.length; i++) {

							int quan = quantity[i];
							double noiseRate = noise[i];

							for (int j = 0; j < quan; j++) {

								Random rand = new Random();

								double max = correctValue + noiseRate
										* correctValue;
								double min = correctValue - noiseRate
										* correctValue;
								double value = rand.nextDouble() * (max - min)
										+ min;
								value = rand.nextDouble();

								valueList.add(value);

								counter++;
							}

							if ((counter + U.firstColumnNumber - 1) > result.length)
								throw new Error(
										"we are generating data columns more than expected");

						}

						U.p("windowCounter: " + windowCounter
								+ " windowIndex: " + windowIndex);

						if (windowCounter <= window) {
							valueList.set(windowIndex, correctValue);
						} else {

							windowIndex++;
							if (windowIndex == counter) {
								windowIndex = 0;
								U.p("counter: " + counter
										+ " start from the first agent again!");
							}
							windowCounter = 1;
							valueList.set(windowIndex, correctValue);
						}
						windowCounter++;

						data = new Data(monitorDate, valueList, accessDate,
								result[2], correctValue);

						// data.print();

						allData.add(data);

					}

				} else

					throw new MyException(
							" Number of elements are not smaller than expected"
									+ inputFile);

			}

			bufferedReader.close();

			Utility.WriteDataFile(allData);

		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");

			try {
				throw new MyException("File is not found " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println(ex.toString());
			System.out.println("Error reading file '" + inputFile + "'");
			try {
				throw new MyException("File error " + inputFile);
			} catch (MyException e) {

				e.printStackTrace();

			}

		} catch (MyException e) {

			e.printStackTrace();
		}

	}

	public void updateagentDataAccessIndex(int agentIndex, int[] colsIndex) {

		for (int j = 0; j < cols; j++) {
			agentDataAccessIndex[agentIndex][j] = colsIndex[j];

		}
	}

	public void giveDataToAgents(int marketNumber) {

		Vector<Double> marketData = allData.elementAt(marketNumber - 1)
				.getValueList();

		SimDate monitoringDay = allData.elementAt(marketNumber - 1)
				.getMonitoringDay();

		SimDate accessDay = null;

		for (int agentIndex = 0; agentIndex < rows; agentIndex++) {
			Vector<Double> values = new Vector<Double>();

			for (int j = 0; j < cols; j++) {

				int index = agentDataAccessIndex[agentIndex][j]
						- U.firstColumnNumber;

				if (index >= 0) {

					double value = marketData.elementAt(index);

					values.add(value);
				}

			}

			Data newData = new Data(monitoringDay, values, accessDay);

			mm.getParticipants().get(agentIndex).setMyData(newData);
		}

	}

	public int[][] getAgentDataAccessIndex() {
		return agentDataAccessIndex;
	}

	public void setAgentDataAccessIndex(int[][] agentDataAccessIndex) {
		this.agentDataAccessIndex = agentDataAccessIndex;
	}

	// returns data.valueList vector for the index of the marketNumber
	public Vector<Double> giveDataList(int marketNumber) {
		Vector<Double> dt = null;

		int index = marketNumber - 1;
		dt = allData.get(index).getValueList();

		return dt;

	}

	// returns true label vector for the index of the marketNumber
	public String giveCorrectLabel(int marketNumber) {

		int index = marketNumber - 1;
		String ans = allData.get(index).getLevel();

		return ans;

	}

	// returns true label vector for the index of the marketNumber
	public double giveCorrectValue(int marketNumber) {

		int index = marketNumber - 1;
		double ans = allData.get(index).getValue();

		return ans;

	}

	// returns true label vector for the index of the marketNumber
	public Vector<SimDate> giveMonitorDates() {

		Vector<SimDate> Dates = new Vector<SimDate>();

		for (int i = 0; i < allData.size(); i++)

			Dates.add(allData.get(i).getMonitoringDay());

		return Dates;

	}

	/*
	 * This function reads a file and takes the element where the index vector
	 * points to, for the values of the data object accessDateColumn must
	 * indicate where is accessDate or -1 if not exist index param must says
	 * which column values should be taken ( count columns from zero), if index
	 * param is null then the second column will be taken as a single value
	 */
	public static Vector<Data> readData(String fileAddress,
			Vector<Integer> dataIndexVector, int accessDateColumn) {
		Vector<Data> dataList;
		String inputFile;

		dataList = new Vector<Data>();
		inputFile = fileAddress;

		String line = null;
 

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(inputFile);

 
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				String[] result = line.split("\\s");// line.split(",");

				if (result.length >= 2) {

 
					// the first column should show when event date 
					SimDate monitorDate = SimDate.convertToDate(result[0]);
					Data data;
					if (monitorDate != null) {
						SimDate accessDate = null;

						// the accessDateColumn should show when these data is accessible
						if (accessDateColumn != -1)
							accessDate = SimDate
									.convertToDate(result[accessDateColumn]);

			 

						if (dataIndexVector != null) {
							// taking the values for the index
							Vector<Double> valueList = new Vector<Double>();
							for (int i = 0; i < dataIndexVector.size(); i++) {
								int col = dataIndexVector.elementAt(i);

								if (Utility.isDouble(result[col]))
									valueList.add(Double
											.parseDouble(result[col]));
							}

							data = new Data(monitorDate, valueList, accessDate);

						}// if ataIndexVector is null, second column will be used
						else if (Utility.isDouble(result[1]))
							data = new Data(monitorDate,
									Double.parseDouble(result[1]), accessDate);

						else
							data = new Data(monitorDate, result[1], accessDate);

						dataList.add(data);
 
					}
				} else
					try {
						throw new MyException(
								" Number of elements are not smaller than expected"
										+ inputFile);
					} catch (MyException e) {
						e.printStackTrace();
					}
			}

			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inputFile + "'");

			try {
				throw new MyException("File is not found " + inputFile);
			} catch (MyException e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			System.out.println("Error reading file '" + inputFile + "'");
			try {
				throw new MyException("File error " + inputFile);
			} catch (MyException e) {
				e.printStackTrace();
			}
 
		}  

		return dataList;

	}



}
