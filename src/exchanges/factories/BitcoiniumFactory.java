package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitcoiniumFactory extends GenericFactory {

	BitcoiniumFactory() {
		exchange = Exchanges.BITCOINIUM;
		ticker_pub = "BITCOINIUM_TICKER_PUB";
		orderbook_pub = "BITCOINIUM_ORDERBOOK_PUB";
	}
}