package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class ItbitFactory extends GenericFactory {

	ItbitFactory() {
		exchange = Exchanges.ITBIT;
		ticker_pub = "ITBIT_TICKER_PUB";
		orderbook_pub = "ITBIT_ORDERBOOK_PUB";
	}
}