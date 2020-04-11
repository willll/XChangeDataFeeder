package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class DragonexFactory extends GenericFactory {

	DragonexFactory() {
		exchange = Exchanges.DRAGONEX;
		ticker_pub = "DRAGONEX_TICKER_PUB";
		orderbook_pub = "DRAGONEX_ORDERBOOK_PUB";
	}
}