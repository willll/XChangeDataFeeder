package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bitcoinaverage.BitcoinAverageExchange;;

public class Bitcoinaverage extends GenericExchange {
	public Bitcoinaverage() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BitcoinAverageExchange.class.getName());
	}
}