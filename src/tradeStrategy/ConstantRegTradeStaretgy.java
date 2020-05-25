package tradeStrategy;

import java.util.HashMap;
import market.MarketMaker;
import Utill.U;
import agents.RegressionAgent;

public class ConstantRegTradeStaretgy extends TradeStrategy_Regression {

	public ConstantRegTradeStaretgy(MarketMaker mm) {

		super(mm);
	}

	public HashMap<Double, Double> giveBasket(RegressionAgent agent) {
		double bid = U.maxRatePerTransaction * agent.getCapital();

		HashMap<Double, Double> basket = new HashMap<Double, Double>();

		basket.put(agent.getCurrentPrediction(), bid);

		return basket;
	}

}
