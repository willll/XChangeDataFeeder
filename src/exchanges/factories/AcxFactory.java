package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class AcxFactory extends GenericFactory {

	AcxFactory() {
		exchange = Exchanges.ACX;
		ticker_pub = "ACX_TICKER_PUB";
		orderbook_pub = "ACX_ORDERBOOK_PUB";
	}
}