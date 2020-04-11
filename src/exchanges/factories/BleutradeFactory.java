package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BleutradeFactory extends GenericFactory {

	BleutradeFactory() {
		exchange = Exchanges.BLEUTRADE;
		ticker_pub = "BLEUTRADE_TICKER_PUB";
		orderbook_pub = "BLEUTRADE_ORDERBOOK_PUB";
	}
}