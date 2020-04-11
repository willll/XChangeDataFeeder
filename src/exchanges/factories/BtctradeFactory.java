package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BtctradeFactory extends GenericFactory {

	BtctradeFactory() {
		exchange = Exchanges.BTCTRADE;
		ticker_pub = "BTCTRADE_TICKER_PUB";
		orderbook_pub = "BTCTRADE_ORDERBOOK_PUB";
	}
}