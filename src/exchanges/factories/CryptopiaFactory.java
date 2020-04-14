package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CryptopiaFactory extends GenericFactory {

	CryptopiaFactory() {
		exchange = Exchanges.CRYPTOPIA;
		ticker_pub = "CRYPTOPIA_TICKER_PUB";
		orderbook_pub = "CRYPTOPIA_ORDERBOOK_PUB";
	}
}