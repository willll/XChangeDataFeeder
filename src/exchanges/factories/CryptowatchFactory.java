package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CryptowatchFactory extends GenericFactory {

	CryptowatchFactory() {
		exchange = Exchanges.CRYPTOWATCH;
		ticker_pub = "CRYPTOWATCH_TICKER_PUB";
		orderbook_pub = "CRYPTOWATCH_ORDERBOOK_PUB";
	}
}