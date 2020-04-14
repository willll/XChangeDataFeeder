package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BankeraFactory extends GenericStreamingFactory {

	BankeraFactory() {
		exchange = Exchanges.BANKERA;
		ticker_pub = "BANKERA_TICKER_PUB";
		orderbook_pub = "BANKERA_ORDERBOOK_PUB";
	}
}