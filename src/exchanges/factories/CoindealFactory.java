package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoindealFactory extends GenericFactory {

	CoindealFactory() {
		exchange = Exchanges.COINDEAL;
		ticker_pub = "COINDEAL_TICKER_PUB";
		orderbook_pub = "COINDEAL_ORDERBOOK_PUB";
	}
}