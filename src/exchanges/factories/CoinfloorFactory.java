package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinfloorFactory extends GenericFactory {

	CoinfloorFactory() {
		exchange = Exchanges.COINFLOOR;
		ticker_pub = "COINFLOOR_TICKER_PUB";
		orderbook_pub = "COINFLOOR_ORDERBOOK_PUB";
	}
}