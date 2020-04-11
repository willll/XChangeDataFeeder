package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class WexFactory extends GenericStreamingFactory {

	WexFactory() {
		exchange = Exchanges.WEX;
		ticker_pub = "WEX_TICKER_PUB";
		orderbook_pub = "WEX_ORDERBOOK_PUB";
	}
}