package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CryptonitFactory extends GenericFactory {

	CryptonitFactory() {
		exchange = Exchanges.CRYPTONIT;
		ticker_pub = "CRYPTONIT_TICKER_PUB";
		orderbook_pub = "CRYPTONIT_ORDERBOOK_PUB";
	}
}