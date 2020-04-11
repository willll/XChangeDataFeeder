package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitcointoyouFactory extends GenericFactory {

	BitcointoyouFactory() {
		exchange = Exchanges.BITCOINTOYOU;
		ticker_pub = "BITCOINTOYOU_TICKER_PUB";
		orderbook_pub = "BITCOINTOYOU_ORDERBOOK_PUB";
	}
}