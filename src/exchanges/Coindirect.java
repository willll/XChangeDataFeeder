package exchanges;

import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.coindirect.CoindirectExchange;

public class Coindirect extends GenericExchange {
	public Coindirect() {
		this.exchange = ExchangeFactory.INSTANCE.createExchange(CoindirectExchange.class.getName());
	}
}