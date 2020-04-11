package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.kraken.KrakenExchange;

public class Kraken extends GenericExchange {
	public Kraken() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(KrakenExchange.class.getName());
	}
}
