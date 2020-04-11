package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LunoFactory extends GenericFactory {

	LunoFactory() {
		exchange = Exchanges.LUNO;
		ticker_pub = "LUNO_TICKER_PUB";
		orderbook_pub = "LUNO_ORDERBOOK_PUB";
	}
}