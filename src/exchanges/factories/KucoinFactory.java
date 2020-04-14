package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class KucoinFactory extends GenericFactory {

	KucoinFactory() {
		exchange = Exchanges.KUCOIN;
		ticker_pub = "KUCOIN_TICKER_PUB";
		orderbook_pub = "KUCOIN_ORDERBOOK_PUB";
	}
}