package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BittrexFactory extends GenericFactory {

	BittrexFactory() {
		exchange = Exchanges.BITTREX;
		ticker_pub = "BITTREX_TICKER_PUB";
		orderbook_pub = "BITTREX_ORDERBOOK_PUB";
		can_handle_multiple_threads = false;
	}
}
