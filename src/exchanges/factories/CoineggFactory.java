package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoineggFactory extends GenericFactory {

	CoineggFactory() {
		exchange = Exchanges.COINEGG;
		ticker_pub = "COINEGG_TICKER_PUB";
		orderbook_pub = "COINEGG_ORDERBOOK_PUB";
	}
}