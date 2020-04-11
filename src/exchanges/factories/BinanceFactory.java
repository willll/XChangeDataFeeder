package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BinanceFactory extends GenericStreamingFactory {

	BinanceFactory() {
		exchange = Exchanges.BINANCE;
		ticker_pub = "BINANCE_TICKER_PUB";
		orderbook_pub = "BINANCE_ORDERBOOK_PUB";
	}
}