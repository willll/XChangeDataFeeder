package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinbeneFactory extends GenericFactory {

	CoinbeneFactory() {
		exchange = Exchanges.COINBENE;
		ticker_pub = "COINBENE_TICKER_PUB";
		orderbook_pub = "COINBENE_ORDERBOOK_PUB";
	}
}