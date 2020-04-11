package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class GlobitexFactory extends GenericFactory {

	GlobitexFactory() {
		exchange = Exchanges.GLOBITEX;
		ticker_pub = "GLOBITEX_TICKER_PUB";
		orderbook_pub = "GLOBITEX_ORDERBOOK_PUB";
	}
}