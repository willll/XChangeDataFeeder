package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitzFactory extends GenericFactory {

	BitzFactory() {
		exchange = Exchanges.BITZ;
		ticker_pub = "BITZ_TICKER_PUB";
		orderbook_pub = "BITZ_ORDERBOOK_PUB";
	}
}