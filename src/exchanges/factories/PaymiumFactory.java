package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class PaymiumFactory extends GenericFactory {

	PaymiumFactory() {
		exchange = Exchanges.PAYMIUM;
		ticker_pub = "PAYMIUM_TICKER_PUB";
		orderbook_pub = "PAYMIUM_ORDERBOOK_PUB";
	}
}