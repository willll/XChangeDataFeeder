package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CcexFactory extends GenericFactory {

	CcexFactory() {
		exchange = Exchanges.CCEX;
		ticker_pub = "CCEX_TICKER_PUB";
		orderbook_pub = "CCEX_ORDERBOOK_PUB";
	}
}