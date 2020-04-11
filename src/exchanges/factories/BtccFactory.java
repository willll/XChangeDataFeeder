package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BtccFactory extends GenericFactory {

	BtccFactory() {
		exchange = Exchanges.BTCC;
		ticker_pub = "BTCC_TICKER_PUB";
		orderbook_pub = "BTCC_ORDERBOOK_PUB";
	}
}