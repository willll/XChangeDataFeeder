package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class UpbitFactory extends GenericFactory {

	UpbitFactory() {
		exchange = Exchanges.UPBIT;
		ticker_pub = "UPBIT_TICKER_PUB";
		orderbook_pub = "UPBIT_ORDERBOOK_PUB";
	}
}