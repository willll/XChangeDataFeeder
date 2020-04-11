package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinmateFactory extends GenericStreamingFactory {

	CoinmateFactory() {
		exchange = Exchanges.COINMATE;
		ticker_pub = "COINMATE_TICKER_PUB";
		orderbook_pub = "COINMATE_ORDERBOOK_PUB";
	}
}