package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class DeribitFactory extends GenericFactory {

	DeribitFactory() {
		exchange = Exchanges.DERIBIT;
		ticker_pub = "DERIBIT_TICKER_PUB";
		orderbook_pub = "DERIBIT_ORDERBOOK_PUB";
	}
}