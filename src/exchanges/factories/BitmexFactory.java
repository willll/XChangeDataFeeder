package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitmexFactory extends GenericStreamingFactory {
	BitmexFactory() {
		exchange = Exchanges.BITMEX;
		ticker_pub = "BITMEX_TICKER_PUB";
		orderbook_pub = "BITMEX_ORDERBOOK_PUB";
	}
}
