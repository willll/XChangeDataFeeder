package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coindeal.CoindealExchange;

public class Coindeal extends GenericExchange {
	public Coindeal() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoindealExchange.class.getName());
	}
}