package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class YobitFactory extends GenericFactory {

	YobitFactory() {
		exchange = Exchanges.YOBIT;
		ticker_pub = "YOBIT_TICKER_PUB";
		orderbook_pub = "YOBIT_ORDERBOOK_PUB";
	}
}