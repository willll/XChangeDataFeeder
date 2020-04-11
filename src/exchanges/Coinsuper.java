package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coinsuper.CoinsuperExchange;

public class Coinsuper extends GenericExchange {
	public Coinsuper() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoinsuperExchange.class.getName());
	}
}