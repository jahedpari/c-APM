package Utill;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import agents.RL_RegAgent;
import weka.core.Attribute;


public class U {
	
	
	public static  double maxRatePerTransaction; 
	public static  double maxRatePerTransaction1= 0.9;   // for first day only 
	public static  double maxRatePerTransaction2= 0.01;   //for all other days
	public static  double minRatePerTransaction=0.001;
	public static  boolean giveRewardinFirstRound=true;   //if you make this false then make maxRatePerTransaction2= 0.9 otherwise 0.01  
	

	
	public	static int minDay=2; // minimum number of Days
	public	static int maxDay=2;  // maximum number of Days
	public	static int daySteps=5; // day step
	public  static int days;   
	
	
	public	static int limit=2000;   //maximum number of records to read
	public	static int ignoreFirstXMarket=5;  // number of initial records to ignore
	public   static int NumberOfRuns=1;  
	
	public static String tradeStrategyName;
	public static String[] tradeStrategyNameVec= {"Q-LearnTradeStaretgy" , "SimpleRegTradeStaretgy", "ConfidenceRlRegTradeStaretgy"};
	
	
	// what to be reported in Pfile--Predictions of first day agent or the second day
   public static final int PfilePredictionIndex = 0; // 0 for first day prediction, 1 for second day prediction


	
	public  static int numberOfActions=2;  
	public  static int numberOfClusters=3;
	public static double Qalpha  =1;  

	
	public static boolean autoRescaleCapital=true;   
	public static double minCapitalThreshold=  1E-20;  //-5, -20
	public static double maxCapitalThreshold=  1E+20;  //20
	public static double newMinCapital=  1E-1;
	public static double newMaxCapital =  1E+1;
	public static int scaleCounter=0;
	
	public static int classifierNumber=1;
	public static boolean forEachClasifier= false;
 
	
    public	static int traininNumber=0;
	public	static String agentAnalysis;

	public static boolean choosePAuto=false;
	public static boolean chooseCAuto=false;
	public static boolean chooseCandPAuto=false;
	
	public static double confidence=0.5; 	
	public static double confidenceAplpha=0.5; 

	
    public static Vector<String> meritAgentsVector= new Vector<String>();  
    public static ArrayList<Integer> meritAgentsCounter= new  ArrayList<Integer>();
    public static int MeritAgentMarketLimit =10; 
	
	public static  boolean  useConfidence;
 

	public  static MathContext mc=MathContext.DECIMAL32;
	
	
	public static int testParam=1;
	public static int firstColumnNumber=5;  //5
	public static  int  lastColumnNumber=0;    // 36;//6 //8;//105
	public static int numberOfDataColumns =lastColumnNumber- firstColumnNumber; 
	public static int numberOfAgents=100;//11;
  
	
 
	public static boolean writeBfile=false; 
	public static boolean writeTfile=false;
	public static boolean writeTimefile=false;
	public static boolean writeTime2file=false;
	public static boolean writeMAfile=false;
	public static boolean writePredfile=false;
	public static boolean writeRlFile= false;
	public static boolean writeRlFileForEachAgent=false;
	public static boolean WriteActionStatistic=false;
	public static boolean WriteActionStatistic2= false;	
	
	
	public static int tradeStrategyClassifier;
	
 
 
	
    public static  double learninRate=0.1;
	
	public static int RevenueFunction    = 3;     //1=power  2=root  3=percentage error and logartmic scoring rule
	public static double RevenuePower =  Math.E;//7//1;
	public static double RevenueCuttOffParam=100; //60
	public static double beta=100; 
		
	

	
	public static boolean giveAgentsData_allSame=false;  //false
	public static int    giveAgentsData_colNumber=18;  //=> 19 for high quality, 45 for medium, 18 for bad
	
	public static boolean giveAgentsData_perTheirName=true; //true
	public static boolean giveAgentsData_multiAtt_allData=false; //true;
	public static boolean giveAgentsData_multiAtt_byChance=false;
	public static boolean giveAgentsData_Sequence=false;

	
	public static int maxSizeforAgentDataAccess ;	 
	
	
	// Option 1: for all (each predictions* its investments )/ sum of investments; 
	//option2: chooses the prediction with highest investment, ; 
	//Option3=same as option 1, but use a percentage of agents prediction, using the threshold
	public static int marketIntegrationMethod =1;    
	public static int marketIntegrationMethodTreshold=10; //to be used with market integration method=3
	public static int minMITrheshold=20;
	public static int maxMITrheshold=100;
	public static int stepMITrheshold=20;


 public static String firstNote="";
	
	//to store the prediction of market in order to calculate MAE and MSE
	  static final int ROWS = limit;
	    static final int COLS = limit;

	    
	 
 
    public static double[][] preds1 = new double[ROWS][COLS];
    
    
    public static  int currentCol=0;
	

	public static String delim="\t";
    public static float EW_alpha=(float) 0.9;         //step size
    public static float fa_alpha=(float) 0.9;  
    public static String prModel;  

    public static  float reward=(float)1;
    public static ArrayList<Attribute> atts;

	public static int marketNumber;  
	
	
	// for altering agent during the experiment
	public static boolean agentPartialDeletionTest=false;
	public static  boolean agentLateAdditionTest=false;
	public static boolean addPartialNoiseTest=false;
	

	public	static double money=10;
 

	public static boolean includeMoaAgents=false;
	public static boolean includePart1=false;
	public static boolean includePart2=false;
	public static boolean includePart3=false;
	
	public static boolean LeaderAgents=false;
	public static boolean includeRLAgents=false;
	public static boolean includeStdevAgents=false;
	public static boolean includea0Agent=false;

	//must be -1 if not sequence mining or a number to show number of elements for sequence mining
	public	static int ElemNumberforSequenceMining=-1; 
	
	
	//for validation test
	public static boolean testRubbishData=false;
	public static boolean testAbsoluteTrueData=false;
	public static  String ExperimentNote; 

	public	static float minAlpha; 
	public	static float maxAlpha;
	public	static float alphaStep;
	
	
	//to be used for kernel function marketMaker_r giveRevenues()
	public static double scaledMin=0; 
	public static double scaledMax;  
	public static double kernelHeight=2;
	public static double kernelC=2;
	public static double alpha;
	
	
	public static Random randomGenerator = new Random();
	// r=min+ (randomGenerator.nextDouble()* (max- min)) ; 
	//r=min+ U.randomGenerator.nextInt((max- min+1)) ;	
	
	public static int startExperiment;
	
 
    //100 agents
	public static String dataFile2l="Data/allNoNull-100NearMedian.txt";
	public static String dataFile3l="Data/allNoNull-100MedianAndGood.txt";
 
	public static String dataFile4l="Data/allNoNull-100MedianAndBad(v3).txt";
 	public static String dataFile5l="Data/allNoNull-100AllRange.txt";
	
 
	public static String dataFile2="Data/Medium.txt";
	public static String dataFile3="Data/MediumHigh.txt";
	public static String dataFile4="Data/MediumLow.txt";
	public static String dataFile5="Data/lowMediumHigh.txt";
	
	
	public static String[]  dataFileArr={dataFile2,dataFile3,dataFile4,dataFile5};

	//100 agents
	public static String dataFileSm2="Data/NearMedian-finalPrediction.txt";
	public static String dataFileSm3="Data/MedianAndGood-finalPrediction.txt";
	public static String dataFileSm4="Data/MedianAndBad-finalPrediction.txt";
	public static String dataFileSm5="Data/AllRange-finalPrediction.txt";
	
 
	
	 public static String[]  dataFileArrSm={dataFileSm2,dataFileSm3,dataFileSm4,dataFileSm5};
 
	public static String dataFile0="Data/scenario5-8(allNoNull-100AllRange).txt";
	public static String dataFile6="Data/5leaderAnd95Random.txt";
	public static String dataFile7="Data/1leaderAnd100Random.txt";
	public static String dataFile8="Data/randomNumbers.txt";
	public static String dataFile9="Data/oneHighQuality.txt";
	public static String dataFile10="Data/oneMediumQuality.txt";
	public static String dataFile11="Data/oneLowQuality.txt";
	public static String dataFile12="Data/3agentawith3quality.txt";
	public static String dataFile110="Data/testFile3agent.txt";
	public static String dataFile111="Data/1HighQuality-usaGFT.txt";  //final Prediction
	public static String dataFile112="Data/usaGFTOriginal-finalPr-.txt"; //original prediction for scenario 7
	public static String dataFile113="Data/1HighQuality-10classifire-finalPR.txt";  //final Prediction
	public static String dataFile13="Data/50Medium.txt";
	public static String dataFile14="Data/50Medium50Bad2.txt";
	
 
	public static String dataFile15="Data/CompareWithGFT2.txt";
	public static String dataFile16="Data/usingGFT2014.txt";
	public static String dataFile161="Data/GFTandCDC-ACPM.txt";
	public static String dataFile162="Data/GP-GFTandCDC-ACPM-2008-9.txt";
	public static String dataFile163="Data/GP-GFTandCDC-ACPM-2009-10.txt";
	public static String dataFile164="Data/GP-GFTandCDC-ACPM-2010-11.txt";
	public static String dataFile165="Data/GP-GFTandCDC-ACPM-2011-12.txt";
	public static String dataFile166="Data/GP-GFTandCDC-ACPM-2012-13.txt";
	
	public static String[]  dataFileArr_GFT={dataFile162,dataFile163,dataFile164,dataFile165,dataFile166};
	
	public static String dataFile167="Data/GP-GFTandCDC-ACPM-5year.txt";
	public static String dataFile168="Data/Ar-GP-GFTandCDC-ACPM-4year-1weekLag.txt";
	public static String dataFile169="Data/Ar-GP-GFTandCDC-ACPM-4year-2weekLag.txt";
	public static String dataFile170="Data/Ar-GP-GFTandCDC-ACPM-4year-1weekLag-lessClassifier.txt"; //less classifier
 
	public static String dataFile17="Data/UCIFinalPredictionWithR/Cancer-wpbc-data1Feb-R.txt";  
	public static String dataFile18="Data/UCIFinalPredictionWithR/auto-mpg-R.txt";
	public static String dataFile19="Data/UCIFinalPredictionWithR/housing2-R.txt";
	public static String dataFile20="Data/UCIFinalPredictionWithR/ComputerHardware4-R.txt";
	public static String dataFile21="Data/UCIFinalPredictionWithR/yacht_hydrodynamics-R.txt";
	public static String dataFile22="Data/UCIFinalPredictionWithR/ISTANBUL-STOCK-EXCHANGE3-R.txt";
	public static String dataFile23="Data/UCIFinalPredictionWithR/servo-R.txt";  
	public static String dataFile26="Data/UCIFinalPredictionWithR/forestfires2-R.txt";
	public static String dataFile27="Data/UCIFinalPredictionWithR/Automobile-R.txt";
	public static String dataFile29="Data/UCIFinalPredictionWithR/communities-R.txt";	
	public static String dataFile30="Data/UCIFinalPredictionWithR/SolarFlare2-R.txt"; 
	public static String dataFile31="Data/UCIFinalPredictionWithR/airfoil_self_noise1-R.txt";
	public static String dataFile33="Data/UCIFinalPredictionWithR/BikeSharing.txt";	
 
	
	public static String[]  dataFileArr_UCI={dataFile33, dataFile18, dataFile21, dataFile22, dataFile23,dataFile26,dataFile27,dataFile19, dataFile31,dataFile20};
 
	 
	
 
	
 	
 	// use these files only to match number of agents, but create noise from execute file- line 300
	public static String dataFile48="Data/syntheticData-40agents.txt";
	public static String dataFile49="Data/syntheticData-10agents.txt";
	public static String dataFile50="Data/syntheticData-5agents.txt";
	public static String dataFile51="Data/syntheticData-50agents.txt";
	public static String dataFile52="Data/syntheticData-100agents.txt";
	public static String dataFile53="Data/RandomNumber40Agents.txt";
		
	
	 public static String[]  dataFileArr_syntheticData={dataFile48,dataFile49,dataFile50,dataFile51,dataFile52,dataFile53};	
	
			

 	
	public static String[]  dataFileArr_ALL;  
 
	
	 
	
	public static int numberOfClassifiers=0;		
	
	
	public static String[]  dataFileArrIncremental={"Data/IncrementalPopulation/allNoNull-100.txt","Data/IncrementalPopulation/allNoNull-90.txt","Data/IncrementalPopulation/allNoNull-80.txt", "Data/IncrementalPopulation/allNoNull-70.txt", "Data/IncrementalPopulation/allNoNull-60.txt","Data/IncrementalPopulation/allNoNull-50.txt", "Data/IncrementalPopulation/allNoNull-40.txt","Data/IncrementalPopulation/allNoNull-30.txt","Data/IncrementalPopulation/allNoNull-20.txt","Data/IncrementalPopulation/allNoNull-10.txt"};
	public static String[]  dataFileArrIncremental2={"Data/IncrementalPopulation/allNoNull-5.txt", "Data/IncrementalPopulation/allNoNull-3.txt", "Data/IncrementalPopulation/allNoNull-1.txt"};
	public static double confidenceRate=0.5;



	public static int actionStatistic   [][]= new int[numberOfAgents][numberOfActions];	 
	public static int actionStatistic2 [][]= new int[limit][numberOfAgents];

	public static int numberofRecords;

	public static String fileInfo="";



	U ()
	{}

	public static void p(String str)
	{
		System.out.println(str);
	}
	
	public static void pn(String str)
	{
		System.out.print(str);
	}
	
	public static <E> void p(Vector<E> v )
	{
		System.out.print( " [ ");
		for(int i=0; i< v.size(); i++)
		{
		System.out.print(v.elementAt(i)+ "  ");
		}
		System.out.print( " ] \n");
	}
	
	
	public static  void p(double[ ] v )
	{
		System.out.print( " [ ");
		for(int i=0; i< v.length; i++)
		{
		System.out.print(v[i]+ "  ");
		}
		System.out.print( " ] \n");
	}
	
	public static String convertToString(double[ ] v )
	{
		String tmp= " [ ";
		for(int i=0; i< v.length; i++)
		{
			tmp+= v[i]+ "  ";
		}
		tmp+=" ] \n";
		
		return tmp;
	}
	
	
	public static <E> String convertToString(Vector<E>  v )
	{
		String tmp= " [ ";
		for(int i=0; i< v.size(); i++)
		{
			tmp+= v.elementAt(i)+ "  ";
		}
		tmp+=" ] \n";
		
		return tmp;
	}
	
	
	
	public static String convertToString(BigDecimal[ ] v )
	{
		String tmp= " [ ";
		for(int i=0; i< v.length; i++)
		{
			tmp+= v[i]+ "  ";
		}
		tmp+=" ] \n";
		
		return tmp;
	}
	
	public static String getActionStatistic ()
	{
 
	 
		String	output ="PreservePr Action  \t ChangePr Action";
			
		for(int i=0; i<numberOfAgents; i++)
		{
			output+="\n";
			for(int j=0; j<numberOfActions; j++)
				output+=actionStatistic[i][j]+" \t";
		}
		
		return output;
		
	}
	
	
	public static String getActionStatistic2()
	{
		String output="";
		for(int i=0; i<numberOfAgents; i++)
			output+="agent"+i+" \t";
			
		for(int i=0; i<limit; i++)
		{
			output+="\n";
			for(int j=0; j<numberOfAgents; j++)
				output+=actionStatistic2[i][j]+" \t";
		}
		
		return output;
		
	}
	
	public static int getMostFrequentAction(int agentNumber)
	{
		int a=1;
		int maxCount=actionStatistic[agentNumber][0];
		
		for(int j=0; j<numberOfActions; j++)
		{
		  int count= actionStatistic[agentNumber][j];
		  if(count>maxCount)
		  {
			  maxCount=count;
			  a=j+1;
		  }
		  
		}
		
		return a;
	}
	
	
	public static Vector<Integer> getActionFrequency(int agentNumber)
	{
		 Vector<Integer> tmp= new Vector<Integer>();
	
		 for(int j=0; j<numberOfActions; j++)
			{
		     tmp.add(actionStatistic[agentNumber][ j]);
			}
		 
		 return tmp;
	}

	public static void globalSetting ()   
	{
		
		U.maxSizeforAgentDataAccess = U.numberOfDataColumns;
		atts=MyAttribute.makeDefault_r(maxSizeforAgentDataAccess); //2
		 meritAgentsVector=  new Vector<String>(); 
		 
		 meritAgentsCounter= new ArrayList<Integer>();
		 
		U.currentCol=0;
	 
		
		actionStatistic= new int[numberOfAgents][numberOfActions];	
		actionStatistic2= new int[limit][numberOfAgents];	
		
		for(int i=0; i<numberOfAgents; i++)
			for(int j=0; j<numberOfActions; j++)
				 actionStatistic[i][j]=0;
		
		
		for(int i=0; i<limit; i++)
			for(int j=0; j<numberOfAgents; j++)
				 actionStatistic2[i][j]=0;
		
		
		RL_RegAgent.agentNumber=U.firstColumnNumber-1;
		
	}
	
	
	public static  String[]  headers;
	// to set number of records and cols
	 public static void setlastDataColumnNumber (String inputFile)
	{
		 int counter=0; 
		 boolean flag= false;
		try {
			
			FileReader fileReader = new FileReader(inputFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line="";
			
			while ((line = bufferedReader.readLine()) != null)
			{
			
				String[] result = line.split("\\s"); 

				
				if ( flag== false && result.length >= U.firstColumnNumber-1 ) 
				{
					lastColumnNumber=result.length+1   ;  
					 headers=result;
					 
					for(int i=0; i< result.length; i++)
					{
					U.p(i+"  "+ result[i]);
					
					}
				 
					flag= true;
					counter=0;
				}
				
			
				counter++;	
					
			}
					bufferedReader.close();
				
			}  catch (IOException ex)
			{
				System.out.println(ex.toString());
				System.out.println("Error reading file '" + inputFile + "'");
				
				} 
		System.out.println("lastDataColumnNumber: "+ lastColumnNumber);
		
		numberOfDataColumns=lastColumnNumber- firstColumnNumber; 
		maxSizeforAgentDataAccess=numberOfDataColumns;
		
		
		numberofRecords=counter;
		
		
	}

 
	 
	 
	 
		public static String[] append(String[] source1, String[] source2) {
			
			String[] result= new String[source1.length+ source2.length];
				
			 int counter=0;
			 for (int i = 0; i < source1.length; i++) {
				 result[i]=source1[i];
				 counter++;
	         }
			
			 for (int i = 0; i < source2.length; i++) {
				 result[counter]=source2[i];
				 counter++;
	         }
			
			 return result;
		}
		

	public static void p(String[] res) {
		 for (int i = 0; i < res.length; i++) {
			 p(res[i]+"  ");
         }
		
	}

	public static void p(HashMap<Double, Double> hashmap) {
		 
		Iterator<Double> iter= hashmap.keySet().iterator(); 
				 
		 
		
				while(iter.hasNext())
				{
				  double key = iter.next();
		          double sec= hashmap.get(key);
		          
		          U.p( key +" : "+ sec+" \n");
				  
				}
	}

	public static void p(ArrayList<Bid> arr) {
		

		Iterator<Bid> iter= arr.iterator(); 
		 
		 
		
		while(iter.hasNext())
		{
			
			Bid bid= iter.next();
		  double pr = bid.getPrediction();
          double invest= bid.getInvest();
          
          U.p( pr +" : "+ invest+" \n");
		  
		}
		
	}

	public static void p1(ArrayList<Integer> arr) {
		 
		for(int i=0; i<arr.size(); i++){
			U.pn( arr.get(i)+"  " );
		}
		U.p("");
	}
	
	
	
    public static double makePR(Vector<Double> pr, Vector<Double> bet)
    {
		    double sum=0;   
		    double counter=0;
 
 
	       for(int i=0; i< pr.size(); i++)
		   {
 
				
			   double prediction=  pr.elementAt(i);
		
			   
			  if(prediction !=  Double.NaN )
				  
			   {
					 double	 invest=bet.elementAt(i);
 
				  counter+=invest;
 
				   sum+=prediction*invest;
	 
				 
			   }
			 
 
		   }
		   U.p("***********");
		   
		  double  prediction= sum/counter;
		  
		  return prediction;
    }
	
}
