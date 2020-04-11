package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class ExxFactory extends GenericFactory {

	ExxFactory() {
		exchange = Exchanges.EXX;
		ticker_pub = "EXX_TICKER_PUB";
		orderbook_pub = "EXX_ORDERBOOK_PUB";
	}
}