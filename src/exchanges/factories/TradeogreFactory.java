package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class TradeogreFactory extends GenericFactory {

	TradeogreFactory() {
		exchange = Exchanges.TRADEOGRE;
		ticker_pub = "TRADEOGRE_TICKER_PUB";
		orderbook_pub = "TRADEOGRE_ORDERBOOK_PUB";
	}
}