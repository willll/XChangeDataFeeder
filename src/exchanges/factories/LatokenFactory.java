package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LatokenFactory extends GenericFactory {

	LatokenFactory() {
		exchange = Exchanges.LATOKEN;
		ticker_pub = "LATOKEN_TICKER_PUB";
		orderbook_pub = "LATOKEN_ORDERBOOK_PUB";
	}
}