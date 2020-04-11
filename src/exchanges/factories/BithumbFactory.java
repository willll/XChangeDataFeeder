package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BithumbFactory extends GenericFactory {

	BithumbFactory() {
		exchange = Exchanges.BITHUMB;
		ticker_pub = "BITHUMB_TICKER_PUB";
		orderbook_pub = "BITHUMB_ORDERBOOK_PUB";
	}
}