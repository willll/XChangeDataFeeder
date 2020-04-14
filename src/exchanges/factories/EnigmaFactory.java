package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class EnigmaFactory extends GenericFactory {

	EnigmaFactory() {
		exchange = Exchanges.ENIGMA;
		ticker_pub = "ENIGMA_TICKER_PUB";
		orderbook_pub = "ENIGMA_ORDERBOOK_PUB";
	}
}