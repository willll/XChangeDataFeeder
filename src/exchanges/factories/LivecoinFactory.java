package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LivecoinFactory extends GenericFactory {

	LivecoinFactory() {
		exchange = Exchanges.LIVECOIN;
		ticker_pub = "LIVECOIN_TICKER_PUB";
		orderbook_pub = "LIVECOIN_ORDERBOOK_PUB";
	}
}