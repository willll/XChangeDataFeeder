package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoindirectFactory extends GenericFactory {

	CoindirectFactory() {
		exchange = Exchanges.COINDIRECT;
		ticker_pub = "COINDIRECT_TICKER_PUB";
		orderbook_pub = "COINDIRECT_ORDERBOOK_PUB";
	}
}