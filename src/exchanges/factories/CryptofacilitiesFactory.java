package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class CryptofacilitiesFactory extends GenericFactory {

	CryptofacilitiesFactory() {
		exchange = Exchanges.CRYPTOFACILITIES;
		ticker_pub = "CRYPTOFACILITIES_TICKER_PUB";
		orderbook_pub = "CRYPTOFACILITIES_ORDERBOOK_PUB";
	}
}