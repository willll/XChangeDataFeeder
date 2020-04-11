package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitsoFactory extends GenericFactory {

	BitsoFactory() {
		exchange = Exchanges.BITSO;
		ticker_pub = "BITSO_TICKER_PUB";
		orderbook_pub = "BITSO_ORDERBOOK_PUB";
	}
}