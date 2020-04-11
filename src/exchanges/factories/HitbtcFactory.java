package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class HitbtcFactory extends GenericStreamingFactory {

	HitbtcFactory() {
		exchange = Exchanges.HITBTC;
		ticker_pub = "HITBTC_TICKER_PUB";
		orderbook_pub = "HITBTC_ORDERBOOK_PUB";
	}
}