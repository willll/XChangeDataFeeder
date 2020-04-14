package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitflyerFactory extends GenericStreamingFactory {

	BitflyerFactory() {
		exchange = Exchanges.BITFLYER;
		ticker_pub = "BITFLYER_TICKER_PUB";
		orderbook_pub = "BITFLYER_ORDERBOOK_PUB";
	}
}