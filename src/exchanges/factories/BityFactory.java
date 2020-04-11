package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BityFactory extends GenericFactory {

	BityFactory() {
		exchange = Exchanges.BITY;
		ticker_pub = "BITY_TICKER_PUB";
		orderbook_pub = "BITY_ORDERBOOK_PUB";
	}
}