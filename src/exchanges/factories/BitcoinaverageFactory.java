package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitcoinaverageFactory extends GenericFactory {

	BitcoinaverageFactory() {
		exchange = Exchanges.BITCOINAVERAGE;
		ticker_pub = "BITCOINAVERAGE_TICKER_PUB";
		orderbook_pub = "BITCOINAVERAGE_ORDERBOOK_PUB";
	}
}