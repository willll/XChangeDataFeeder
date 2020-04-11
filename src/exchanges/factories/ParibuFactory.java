package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class ParibuFactory extends GenericFactory {

	ParibuFactory() {
		exchange = Exchanges.PARIBU;
		ticker_pub = "PARIBU_TICKER_PUB";
		orderbook_pub = "PARIBU_ORDERBOOK_PUB";
	}
}