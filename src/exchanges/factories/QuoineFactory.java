package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class QuoineFactory extends GenericFactory {

	QuoineFactory() {
		exchange = Exchanges.QUOINE;
		ticker_pub = "QUOINE_TICKER_PUB";
		orderbook_pub = "QUOINE_ORDERBOOK_PUB";
	}
}