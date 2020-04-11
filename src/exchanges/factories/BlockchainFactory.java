package exchanges.factories;

import exchanges.factories.EntryPoint.Exchanges;

public class BlockchainFactory extends GenericFactory {

	BlockchainFactory() {
		exchange = Exchanges.BLOCKCHAIN;
		ticker_pub = "BLOCKCHAIN_TICKER_PUB";
		orderbook_pub = "BLOCKCHAIN_ORDERBOOK_PUB";
	}
}