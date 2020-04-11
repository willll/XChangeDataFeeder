package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class MercadobitcoinFactory extends GenericFactory {

	MercadobitcoinFactory() {
		exchange = Exchanges.MERCADOBITCOIN;
		ticker_pub = "MERCADOBITCOIN_TICKER_PUB";
		orderbook_pub = "MERCADOBITCOIN_ORDERBOOK_PUB";
	}
}