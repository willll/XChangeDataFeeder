package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class DvchainFactory extends GenericFactory {

	DvchainFactory() {
		exchange = Exchanges.DVCHAIN;
		ticker_pub = "DVCHAIN_TICKER_PUB";
		orderbook_pub = "DVCHAIN_ORDERBOOK_PUB";
	}
}