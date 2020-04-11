package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinexFactory extends GenericFactory {

	CoinexFactory() {
		exchange = Exchanges.COINEX;
		ticker_pub = "COINEX_TICKER_PUB";
		orderbook_pub = "COINEX_ORDERBOOK_PUB";
	}
}