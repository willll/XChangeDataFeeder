package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.blockchain.BlockchainExchange;

public class Blockchain extends GenericExchange {
	public Blockchain() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BlockchainExchange.class.getName());
	}
}