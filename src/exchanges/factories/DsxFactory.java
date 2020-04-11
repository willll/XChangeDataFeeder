package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class DsxFactory extends GenericFactory {

	DsxFactory() {
		exchange = Exchanges.DSX;
		ticker_pub = "DSX_TICKER_PUB";
		orderbook_pub = "DSX_ORDERBOOK_PUB";
	}
}