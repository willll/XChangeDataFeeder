package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitcoinde.BitcoindeExchange;

public class Bitcoinde extends GenericExchange {
	public Bitcoinde() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitcoindeExchange.class.getName());
	}
}