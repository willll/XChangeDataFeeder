package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitcointoyou.BitcointoyouExchange;

public class Bitcointoyou extends GenericExchange {
	public Bitcointoyou() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitcointoyouExchange.class.getName());
	}
}