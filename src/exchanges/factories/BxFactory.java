package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BxFactory extends GenericFactory {

	BxFactory() {
		exchange = Exchanges.BX;
		ticker_pub = "BX_TICKER_PUB";
		orderbook_pub = "BX_ORDERBOOK_PUB";
	}
}