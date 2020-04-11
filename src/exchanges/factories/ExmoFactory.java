package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class ExmoFactory extends GenericFactory {

	ExmoFactory() {
		exchange = Exchanges.EXMO;
		ticker_pub = "EXMO_TICKER_PUB";
		orderbook_pub = "EXMO_ORDERBOOK_PUB";
	}
}