package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CampbxFactory extends GenericFactory {

	CampbxFactory() {
		exchange = Exchanges.CAMPBX;
		ticker_pub = "CAMPBX_TICKER_PUB";
		orderbook_pub = "CAMPBX_ORDERBOOK_PUB";
	}
}