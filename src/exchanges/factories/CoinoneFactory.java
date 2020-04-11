package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinoneFactory extends GenericFactory {

	CoinoneFactory() {
		exchange = Exchanges.COINONE;
		ticker_pub = "COINONE_TICKER_PUB";
		orderbook_pub = "COINONE_ORDERBOOK_PUB";
	}
}