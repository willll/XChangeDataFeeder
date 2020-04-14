package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bittrex.BittrexExchange;

public class Bittrex extends GenericExchange {
	public Bittrex() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(BittrexExchange.class.getName());
	}
}