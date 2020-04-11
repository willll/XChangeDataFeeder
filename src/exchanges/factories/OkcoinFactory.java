package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class OkcoinFactory extends GenericStreamingFactory {

	OkcoinFactory() {
		exchange = Exchanges.OKCOIN;
		ticker_pub = "OKCOIN_TICKER_PUB";
		orderbook_pub = "OKCOIN_ORDERBOOK_PUB";
	}
}