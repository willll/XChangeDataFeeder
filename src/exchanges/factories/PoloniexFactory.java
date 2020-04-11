package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class PoloniexFactory extends GenericStreamingFactory {

	PoloniexFactory() {
		exchange = Exchanges.POLONIEX;
		ticker_pub = "POLONIEX_TICKER_PUB";
		orderbook_pub = "POLONIEX_ORDERBOOK_PUB";
	}
}