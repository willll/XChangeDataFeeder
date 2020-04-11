package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CobinhoodFactory extends GenericFactory {

	CobinhoodFactory() {
		exchange = Exchanges.COBINHOOD;
		ticker_pub = "COBINHOOD_TICKER_PUB";
		orderbook_pub = "COBINHOOD_ORDERBOOK_PUB";
	}
}