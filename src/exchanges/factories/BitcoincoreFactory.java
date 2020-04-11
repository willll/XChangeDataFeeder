package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitcoincoreFactory extends GenericFactory {

	BitcoincoreFactory() {
		exchange = Exchanges.BITCOINCORE;
		ticker_pub = "BITCOINCORE_TICKER_PUB";
		orderbook_pub = "BITCOINCORE_ORDERBOOK_PUB";
	}
}