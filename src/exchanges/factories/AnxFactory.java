package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class AnxFactory extends GenericFactory {

	AnxFactory() {
		exchange = Exchanges.ANX;
		ticker_pub = "ANX_TICKER_PUB";
		orderbook_pub = "ANX_ORDERBOOK_PUB";
	}
}