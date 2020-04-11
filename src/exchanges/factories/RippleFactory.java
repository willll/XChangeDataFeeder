package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class RippleFactory extends GenericFactory {

	RippleFactory() {
		exchange = Exchanges.RIPPLE;
		ticker_pub = "RIPPLE_TICKER_PUB";
		orderbook_pub = "RIPPLE_ORDERBOOK_PUB";
	}
}