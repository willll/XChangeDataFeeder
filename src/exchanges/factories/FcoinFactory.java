package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class FcoinFactory extends GenericFactory {

	FcoinFactory() {
		exchange = Exchanges.FCOIN;
		ticker_pub = "FCOIN_TICKER_PUB";
		orderbook_pub = "FCOIN_ORDERBOOK_PUB";
	}
}