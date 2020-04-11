package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitstampFactory extends GenericStreamingFactory {

	BitstampFactory() {
		exchange = Exchanges.BITSTAMP;
		ticker_pub = "BITSTAMP_TICKER_PUB";
		orderbook_pub = "BITSTAMP_ORDERBOOK_PUB";
	}
}