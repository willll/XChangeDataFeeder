package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BtcmarketsFactory extends GenericFactory {

	BtcmarketsFactory() {
		exchange = Exchanges.BTCMARKETS;
		ticker_pub = "BTCMARKETS_TICKER_PUB";
		orderbook_pub = "BTCMARKETS_ORDERBOOK_PUB";
	}
}