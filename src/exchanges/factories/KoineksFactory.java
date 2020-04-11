package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class KoineksFactory extends GenericFactory {

	KoineksFactory() {
		exchange = Exchanges.KOINEKS;
		ticker_pub = "KOINEKS_TICKER_PUB";
		orderbook_pub = "KOINEKS_ORDERBOOK_PUB";
	}
}