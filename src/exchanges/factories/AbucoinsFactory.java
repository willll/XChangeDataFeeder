package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class AbucoinsFactory extends GenericFactory {

	AbucoinsFactory() {
		exchange = Exchanges.ABUCOINS;
		ticker_pub = "ABUCOINS_TICKER_PUB";
		orderbook_pub = "ABUCOINS_ORDERBOOK_PUB";
	}
}