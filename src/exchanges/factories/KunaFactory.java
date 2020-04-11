package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class KunaFactory extends GenericFactory {

	KunaFactory() {
		exchange = Exchanges.KUNA;
		ticker_pub = "KUNA_TICKER_PUB";
		orderbook_pub = "KUNA_ORDERBOOK_PUB";
	}
}