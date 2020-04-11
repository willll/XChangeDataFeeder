package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.exmo.ExmoExchange;

public class Exmo extends GenericExchange {
	public Exmo() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(ExmoExchange.class.getName());
	}
}