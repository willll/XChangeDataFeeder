package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitbayFactory extends GenericFactory {

	BitbayFactory() {
		exchange = Exchanges.BITBAY;
		ticker_pub = "BITBAY_TICKER_PUB";
		orderbook_pub = "BITBAY_ORDERBOOK_PUB";
	}
}