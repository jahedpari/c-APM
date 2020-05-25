import java.util.Vector;

import market.MarketMaker;
import prediction.PredictionModel;
import prediction.simplePrediction;
import Utill.U;
import agents.AbstractAgent;
import agents.RL_RegAgent;

public class AgentCreation {

	public AgentCreation() {

	}

	// **************************
	// **** creating agents
	// *********
	static Vector<AbstractAgent> createAgents(MarketMaker mm) {

		System.out.println("Agents are being created..");

		Vector<AbstractAgent> agentList = new Vector<AbstractAgent>();

		String tradeStrategyName = U.tradeStrategyName;
	 
		PredictionModel prModel = null;

	
		

		for (int agentNumber = U.firstColumnNumber; agentNumber < U.lastColumnNumber; agentNumber++) {
			
			
				String classifier = "";
				String name = "";
				if (U.giveAgentsData_allSame == true)
					name = "a" + U.giveAgentsData_colNumber + "_"
							+ tradeStrategyName + classifier;
				else
				{ U.p(U.headers[agentNumber-1]);
					name = "a"  + agentNumber+ "-"+U.headers[agentNumber-1] + classifier;
					
				}

				if (U.giveAgentsData_perTheirName == true) {
					RL_RegAgent agent = new RL_RegAgent(mm,  
							tradeStrategyName, U.confidence, name);
					prModel = new simplePrediction(mm);
					agent.setPrModel(prModel);
					agentList.add(agent);
				}

			 

		}

		return agentList;
	} // end function

} // end class
