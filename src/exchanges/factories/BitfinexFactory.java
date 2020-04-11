package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitfinexFactory extends GenericStreamingFactory {
	BitfinexFactory() {
		exchange = Exchanges.BITFINEX;
		ticker_pub = "BITFINEX_TICKER_PUB";
		orderbook_pub = "BITFINEX_ORDERBOOK_PUB";
	}
}
