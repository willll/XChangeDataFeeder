package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitcoindeFactory extends GenericFactory {

	BitcoindeFactory() {
		exchange = Exchanges.BITCOINDE;
		ticker_pub = "BITCOINDE_TICKER_PUB";
		orderbook_pub = "BITCOINDE_ORDERBOOK_PUB";
	}
}