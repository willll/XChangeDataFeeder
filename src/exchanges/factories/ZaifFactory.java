package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class ZaifFactory extends GenericFactory {

	ZaifFactory() {
		exchange = Exchanges.ZAIF;
		ticker_pub = "ZAIF_TICKER_PUB";
		orderbook_pub = "ZAIF_ORDERBOOK_PUB";
	}
}