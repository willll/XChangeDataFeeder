package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class TherockFactory extends GenericFactory {

	TherockFactory() {
		exchange = Exchanges.THEROCK;
		ticker_pub = "THEROCK_TICKER_PUB";
		orderbook_pub = "THEROCK_ORDERBOOK_PUB";
	}
}