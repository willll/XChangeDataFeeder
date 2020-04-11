package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class QuadrigacxFactory extends GenericFactory {

	QuadrigacxFactory() {
		exchange = Exchanges.QUADRIGACX;
		ticker_pub = "QUADRIGACX_TICKER_PUB";
		orderbook_pub = "QUADRIGACX_ORDERBOOK_PUB";
	}
}