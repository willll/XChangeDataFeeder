package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class Bl3pFactory extends GenericFactory {

	Bl3pFactory() {
		exchange = Exchanges.BL3P;
		ticker_pub = "BL3P_TICKER_PUB";
		orderbook_pub = "BL3P_ORDERBOOK_PUB";
	}
}