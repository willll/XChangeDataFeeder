package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BiboxFactory extends GenericFactory {

	BiboxFactory() {
		exchange = Exchanges.BIBOX;
		ticker_pub = "BIBOX_TICKER_PUB";
		orderbook_pub = "BIBOX_ORDERBOOK_PUB";
	}
}