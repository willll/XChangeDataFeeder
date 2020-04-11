package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitmarketFactory extends GenericFactory {

	BitmarketFactory() {
		exchange = Exchanges.BITMARKET;
		ticker_pub = "BITMARKET_TICKER_PUB";
		orderbook_pub = "BITMARKET_ORDERBOOK_PUB";
	}
}