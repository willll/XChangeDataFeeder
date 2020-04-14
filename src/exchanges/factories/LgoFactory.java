package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LgoFactory extends GenericStreamingFactory {

	LgoFactory() {
		exchange = Exchanges.LGO;
		ticker_pub = "LGO_TICKER_PUB";
		orderbook_pub = "LGO_ORDERBOOK_PUB";
	}
}