package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class TruefxFactory extends GenericFactory {

	TruefxFactory() {
		exchange = Exchanges.TRUEFX;
		ticker_pub = "TRUEFX_TICKER_PUB";
		orderbook_pub = "TRUEFX_ORDERBOOK_PUB";
	}
}