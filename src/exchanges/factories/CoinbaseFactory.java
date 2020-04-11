package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinbaseFactory extends GenericFactory {

	CoinbaseFactory() {
		exchange = Exchanges.COINBASE;
		ticker_pub = "COINBASE_TICKER_PUB";
		orderbook_pub = "COINBASE_ORDERBOOK_PUB";
	}
}