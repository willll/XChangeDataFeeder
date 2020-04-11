package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinbaseproFactory extends GenericStreamingFactory {

	CoinbaseproFactory() {
		exchange = Exchanges.COINBASEPRO;
		ticker_pub = "COINBASEPRO_TICKER_PUB";
		orderbook_pub = "COINBASEPRO_ORDERBOOK_PUB";
	}
}