package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class OpenexchangeratesFactory extends GenericFactory {

	OpenexchangeratesFactory() {
		exchange = Exchanges.OPENEXCHANGERATES;
		ticker_pub = "OPENEXCHANGERATES_TICKER_PUB";
		orderbook_pub = "OPENEXCHANGERATES_ORDERBOOK_PUB";
	}
}