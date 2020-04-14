package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LiquiFactory extends GenericFactory {

	LiquiFactory() {
		exchange = Exchanges.LIQUI;
		ticker_pub = "LIQUI_TICKER_PUB";
		orderbook_pub = "LIQUI_ORDERBOOK_PUB";
	}
}