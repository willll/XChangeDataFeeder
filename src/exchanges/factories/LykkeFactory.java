package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class LykkeFactory extends GenericFactory {

	LykkeFactory() {
		exchange = Exchanges.LYKKE;
		ticker_pub = "LYKKE_TICKER_PUB";
		orderbook_pub = "LYKKE_ORDERBOOK_PUB";
	}
}