package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class KrakenFactory extends GenericFactory {

	KrakenFactory() {
		exchange = Exchanges.KRAKEN;
		ticker_pub = "KRAKEN_TICKER_PUB";
		orderbook_pub = "KRAKEN_ORDERBOOK_PUB";
	}
}
