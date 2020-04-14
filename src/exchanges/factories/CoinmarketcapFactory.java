package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CoinmarketcapFactory extends GenericFactory {

	CoinmarketcapFactory() {
		exchange = Exchanges.COINMARKETCAP;
		ticker_pub = "COINMARKETCAP_TICKER_PUB";
		orderbook_pub = "COINMARKETCAP_ORDERBOOK_PUB";
	}
}