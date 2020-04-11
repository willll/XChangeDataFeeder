package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoingiFactory extends GenericFactory {

	CoingiFactory() {
		exchange = Exchanges.COINGI;
		ticker_pub = "COINGI_TICKER_PUB";
		orderbook_pub = "COINGI_ORDERBOOK_PUB";
	}
}