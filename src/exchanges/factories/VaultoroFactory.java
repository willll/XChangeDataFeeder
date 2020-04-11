package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class VaultoroFactory extends GenericFactory {

	VaultoroFactory() {
		exchange = Exchanges.VAULTORO;
		ticker_pub = "VAULTORO_TICKER_PUB";
		orderbook_pub = "VAULTORO_ORDERBOOK_PUB";
	}
}