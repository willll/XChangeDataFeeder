package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class VircurexFactory extends GenericFactory {

	VircurexFactory() {
		exchange = Exchanges.VIRCUREX;
		ticker_pub = "VIRCUREX_TICKER_PUB";
		orderbook_pub = "VIRCUREX_ORDERBOOK_PUB";
	}
}