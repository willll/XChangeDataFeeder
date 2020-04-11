package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class IdexFactory extends GenericFactory {

	IdexFactory() {
		exchange = Exchanges.IDEX;
		ticker_pub = "IDEX_TICKER_PUB";
		orderbook_pub = "IDEX_ORDERBOOK_PUB";
	}
}