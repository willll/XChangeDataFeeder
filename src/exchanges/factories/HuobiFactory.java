package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class HuobiFactory extends GenericFactory {

	HuobiFactory() {
		exchange = Exchanges.HUOBI;
		ticker_pub = "HUOBI_TICKER_PUB";
		orderbook_pub = "HUOBI_ORDERBOOK_PUB";
	}
}