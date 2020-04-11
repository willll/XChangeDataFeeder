package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinsuperFactory extends GenericFactory {

	CoinsuperFactory() {
		exchange = Exchanges.COINSUPER;
		ticker_pub = "COINSUPER_TICKER_PUB";
		orderbook_pub = "COINSUPER_ORDERBOOK_PUB";
	}
}