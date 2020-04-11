package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LakebtcFactory extends GenericFactory {

	LakebtcFactory() {
		exchange = Exchanges.LAKEBTC;
		ticker_pub = "LAKEBTC_TICKER_PUB";
		orderbook_pub = "LAKEBTC_ORDERBOOK_PUB";
	}
}