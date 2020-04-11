package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class KoinimFactory extends GenericFactory {

	KoinimFactory() {
		exchange = Exchanges.KOINIM;
		ticker_pub = "KOINIM_TICKER_PUB";
		orderbook_pub = "KOINIM_ORDERBOOK_PUB";
	}
}