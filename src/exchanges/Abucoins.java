package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.abucoins.AbucoinsExchange;

public class Abucoins extends GenericExchange {
	public Abucoins() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(AbucoinsExchange.class.getName());
	}
}