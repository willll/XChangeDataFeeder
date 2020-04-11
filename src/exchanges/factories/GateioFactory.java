package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class GateioFactory extends GenericFactory {

	GateioFactory() {
		exchange = Exchanges.GATEIO;
		ticker_pub = "GATEIO_TICKER_PUB";
		orderbook_pub = "GATEIO_ORDERBOOK_PUB";
	}
}