package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BitcoinchartsFactory extends GenericFactory {

	BitcoinchartsFactory() {
		exchange = Exchanges.BITCOINCHARTS;
		ticker_pub = "BITCOINCHARTS_TICKER_PUB";
		orderbook_pub = "BITCOINCHARTS_ORDERBOOK_PUB";
	}
}