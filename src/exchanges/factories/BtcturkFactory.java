package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BtcturkFactory extends GenericFactory {

	BtcturkFactory() {
		exchange = Exchanges.BTCTURK;
		ticker_pub = "BTCTURK_TICKER_PUB";
		orderbook_pub = "BTCTURK_ORDERBOOK_PUB";
	}
}